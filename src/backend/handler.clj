(ns backend.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [backend.routes.home :refer [home-routes]]
            [backend.rest.endpoints :refer [rest-routes]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.json :refer [wrap-json-body]])
  (:use compojure.core)
  (:use ring.util.response))

(defn init []
  (println "backend is starting"))

(defn destroy []
  (println "backend is shutting down"))

(defroutes app-routes
           (route/resources "/")
           (route/not-found "Not Found"))

(def app
   (-> (routes home-routes rest-routes app-routes)
   (handler/site)
   (wrap-json-body)
   (wrap-json-response)
   (wrap-base-url)) )
