(ns backend.rest.endpoints
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [compojure.core :refer :all]
            [backend.views.layout :as layout]
            [ring.util.response :refer [response]]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]))

(def db-spec {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname "//localhost:5432/training"
              :user "kpax"
              :password "6kwwW3cV" })

(defn pool [config]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname config))
               (.setJdbcUrl (str "jdbc:" (:subprotocol config) ":" (:subname config)))
               (.setUser (:user config))
               (.setPassword (:password config))
               (.setMaxPoolSize 4)
               (.setMinPoolSize 1)
               (.setInitialPoolSize 1))]
    {:datasource cpds}))

(def pooled-db (delay (pool db-spec)))

(defn db-connection [] @pooled-db)

(defn read-masterdata-by-type [type]
  (response {:type type}))

(defn read-masterdata-by-id [id]
  (response {:id id}))

(defn update-masterdata-by-id [id body]
  (response {:id id :op "update"}))

(defn delete-masterdata-by-id [id]
  (response {:id id :op "delete"}))

(defn create-masterdata-record [body]
  (jdbc/insert! (db-connection) :test (json/read-str body :key-fn keyword))
  (response {:op "create"}))

(defn ping []
  (json/write-str {:response "pong"}))

(defroutes rest-routes
 (context "/api" [] (defroutes api-routes
    (context "/masterdata" [] (defroutes masterdata-routes
      (POST "/" {body :body} (create-masterdata-record (slurp body)))
      (context "/read" [] (defroutes masterdata-route-read-type
        (GET "/:type" [type] (read-masterdata-by-type type))))
      (context "/:id" [id] (defroutes masterdata-rud-routes
        (GET "/" [] (read-masterdata-by-id id))
        (PUT "/" {body :body} (update-masterdata-by-id id (slurp body)))
        (DELETE "/" [] (delete-masterdata-by-id id))))))))
 (context "/admin" [] (defroutes admin
   (GET "/ping" [] (ping)))))