(ns backend.rest.endpoints
  (:require [compojure.core :refer :all]
            [backend.views.layout :as layout]
            [ring.util.response :refer [response]]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [backend.repos.master.master-data :as master-data-repo]
            [backend.repos.master.master-type :as master-type-repo]))

(defn ping []
  (json/write-str {:response "pong"}))

(defn read-json [request]
  (json/read-str request :key-fn keyword))

(defn id-to-int [id]
  (Integer/valueOf id))

(defroutes rest-routes
 (context "/api" [] (defroutes api-routes
    (context "/masterdata" [] (defroutes masterdata-routes
      (POST "/" {body :body} (master-data-repo/create-masterdata-record (slurp body)))
      (context "/read" [] (defroutes masterdata-route-read-type
        (GET "/:type" [type] (master-type-repo/read-masterdata-by-type type))))
      (context "/:id" [id] (defroutes masterdata-rud-routes
        (GET "/" [] (master-data-repo/read-masterdata-by-id id))
        (PUT "/" {body :body} (master-data-repo/update-masterdata-by-id id (slurp body)))
        (DELETE "/" [] (master-data-repo/delete-masterdata-by-id id))))
      (context "/type" [] (defroutes masterdata-type-routes
         (POST "/" {body :body} (master-type-repo/create-masterdata-type-route (read-json (slurp body))))
         (GET "/all" [] (master-type-repo/read-all-masterdata-types))
         (context "/:id" [id] (defroutes masterdata-type-rud-routes
            (GET "/" [] (master-type-repo/read-masterdata-type-by-id (id-to-int id)))
            (PUT "/" {body :body} (master-type-repo/update-masterdata-type-by-id (id-to-int id) (read-json (slurp body))))
            (DELETE "/" [] (master-type-repo/delete-masterdata-type-by-id (id-to-int id)))))))))))
 (context "/admin" [] (defroutes admin
   (GET "/ping" [] (ping)))))