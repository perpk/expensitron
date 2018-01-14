(ns master_data_spec
  (:require [clojure.test :refer :all]
            [speclj.core :refer :all]
            [backend.repos.master.master-type :refer :all]
            [backend.repos.master.master-data :refer :all]))

(def record-name "wal-mart")

(describe "testing masterdata record CRUDs"
          (before-all
            (println "start testing...")
            (def type (create-masterdata-type-route {:type "supermarket"})))
          (it "record should not be nil"
              (def record (create-masterdata-record {:name record-name :type_id ((first type) :id)}))
              (should-not (nil? record)))
          (it "record should have an id"
              (should-not (nil? ((first record) :id))))
          (it "record should reference a type"
              (should-not (nil? ((first record) :id))))
          (it "record should reference the type it was created for"
              (should (= ((first type) :id) ((first record) :type_id))))
          (it "record should have a name"
              (should-not (nil? ((first record) :name))))
          (it "record should have the same name as the one it was created with"
              (should (= record-name ((first record) :name))))
          (after-all
            (delete-masterdata-by-id ((first record) :id))
            (delete-masterdata-type-by-id ((first type) :id))
            (println "...finished testing"))
          )
(run-specs)