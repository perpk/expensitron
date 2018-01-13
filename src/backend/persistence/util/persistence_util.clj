(ns backend.persistence.util.persistence-util
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [propertea.core :refer (read-properties)]
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