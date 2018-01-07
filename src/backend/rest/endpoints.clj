(ns backend.rest.endpoints
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [compojure.core :refer :all]
            [backend.views.layout :as layout]
            [ring.util.response :refer [response]]
            [clojure.java.jdbc :as jdbc]
            [java-jdbc.sql :as sql]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [propertea.core :refer (read-properties)]
            [clojure.java.io :as io]))

(def db-connection-properties-file (.getPath (io/resource "db-connection.properties")))

(def db-connection-properties (read-properties db-connection-properties-file))

(def db-spec {:classname (db-connection-properties :classname)
              :subprotocol (db-connection-properties :subprotocol)
              :subname (db-connection-properties :subname)
              :user (db-connection-properties :user)
              :password (db-connection-properties :password) })

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
  (jdbc/query (db-connection) :master_data_item (sql/where {:type_id type})))

(defn read-masterdata-by-id [id]
  (jdbc/query (db-connection) (sql/select * :master_data_item (sql/where {:id (Integer/valueOf id)}))))

(defn update-masterdata-by-id [id body]
  (jdbc/update! (db-connection) :master_data_item (json/read-str body :key-fn keyword) (sql/where {:id (Integer/valueOf id)})))

(defn delete-masterdata-by-id [id]
  (jdbc/delete! (db-connection) :master_data_item (sql/where {:id (Integer/valueOf id)})))

(defn create-masterdata-record [body]
  (jdbc/insert! (db-connection) :master_data_item (json/read-str body :key-fn keyword)))

(defn create-masterdata-type-route [body]
  (jdbc/insert! (db-connection) :master_data_type (json/read-str body :key-fn keyword)))

(defn read-all-masterdata-types []
  (jdbc/query (db-connection) (sql/select * :master_data_type)))

(defn read-masterdata-type-by-id [id]
  (jdbc/query (db-connection) (sql/select * :master_data_type (sql/where {:id (Integer/valueOf id)}))))

(defn update-masterdata-type-by-id [id body]
  (jdbc/update! (db-connection) :master_data_type (json/read-str body :key-fn keyword) (sql/where {:id (Integer/valueOf id)})))

(defn delete-masterdata-type-by-id [id]
  (jdbc/delete! (db-connection) :master_data_type (sql/where {:id (Integer/valueOf id)})))

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
        (DELETE "/" [] (delete-masterdata-by-id id))))
      (context "/type" [] (defroutes masterdata-type-routes
         (POST "/" {body :body} (create-masterdata-type-route (slurp body)))
         (GET "/all" [] (read-all-masterdata-types))
         (context "/:id" [id] (defroutes masterdata-type-rud-routes
            (GET "/" [] (read-masterdata-type-by-id id))
            (PUT "/" {body :body} (update-masterdata-type-by-id id (slurp body)))
            (DELETE "/" [] (delete-masterdata-type-by-id id))))))))))
 (context "/admin" [] (defroutes admin
   (GET "/ping" [] (ping)))))