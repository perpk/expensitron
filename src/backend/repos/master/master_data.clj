(ns backend.repos.master.master-data
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [clojure.java.jdbc :as jdbc]
            [java-jdbc.sql :as sql]
            [clojure.data.json :as json]
            [backend.persistence.util.persistence-util :as persistence]))

(defn read-masterdata-by-id [id]
  (jdbc/query (persistence/db-connection) (sql/select * :master_data_item (sql/where {:id (Integer/valueOf id)}))))

(defn update-masterdata-by-id [id body]
  (jdbc/update! (persistence/db-connection) :master_data_item body  (sql/where {:id id})))

(defn delete-masterdata-by-id [id]
  (jdbc/delete! (persistence/db-connection) :master_data_item (sql/where {:id id})))

(defn create-masterdata-record [body]
  (jdbc/insert! (persistence/db-connection) :master_data_item body))