(ns backend.repos.master.master-type
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [clojure.java.jdbc :as jdbc]
            [java-jdbc.sql :as sql]
            [backend.persistence.util.persistence-util :as persistence]))

(defn read-masterdata-by-type [type]
  (jdbc/query (persistence/db-connection) :master_data_item (sql/where {:type_id type})))

(defn create-masterdata-type-route [body]
  (jdbc/insert! (persistence/db-connection) :master_data_type body))

(defn read-all-masterdata-types []
  (jdbc/query (persistence/db-connection) (sql/select * :master_data_type)))

(defn read-masterdata-type-by-id [id]
  (jdbc/query (persistence/db-connection) (sql/select * :master_data_type (sql/where {:id id}))))

(defn update-masterdata-type-by-id [id body]
  (jdbc/update! (persistence/db-connection) :master_data_type body (sql/where {:id id})))

(defn delete-masterdata-type-by-id [id]
  (jdbc/delete! (persistence/db-connection) :master_data_type (sql/where {:id id})))
