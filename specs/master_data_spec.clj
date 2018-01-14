(ns master_data_spec
  (:require [clojure.test :refer :all]
            [speclj.core :refer :all]
            [backend.repos.master.master-type :refer :all]
            [backend.repos.master.master-data :refer :all]))

(describe "testing masterdata record CRUDs"
          (before-all
            (println "start testing...")
            (def type (create-masterdata-type-route {:type "supermarket"})))
          (it "record should not be nil"
              (def record (create-masterdata-record {:name "wal-mart" :type_id ((first type) :id)}))
              (should-not (nil? record))
              )
          (after-all
            (delete-masterdata-by-id ((first record) :id))
            (delete-masterdata-type-by-id ((first type) :id))
            (println "...finished testing"))
          )
(run-specs)