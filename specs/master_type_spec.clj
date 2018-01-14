(ns master_type_spec
  (:require [clojure.test :refer :all]
            [speclj.core :refer :all]
            [backend.repos.master.master-type :refer :all]
            [backend.repos.master.master-data :refer :all]))

(def type-name "supermarket")

(describe "testing masterdata type CRUDs"
          (before-all
            (println "start testing...")
            (def type (create-masterdata-type-route {:type type-name})))
          (it "record should not be nil"
              (should-not (nil? type)))
          (it "record inside the lazy seq shouldn't also be nil"
              (should-not (nil? (first type))))
          (it "record should have an id"
              (should-not (nil? ((first type) :id))))
          (it "record should have a name"
              (should-not (nil? ((first type) :type))))
          (it "record should have the same name as the one it was created with"
              (should (= type-name ((first type) :type))))
          (after-all
            (delete-masterdata-type-by-id ((first type) :id)))
          )
(run-specs)
