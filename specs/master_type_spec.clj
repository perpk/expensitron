(ns master_type_spec
  (:require [clojure.test :refer :all]
            [speclj.core :refer :all]
            [backend.repos.master.master-type :refer :all]
            [backend.repos.master.master-data :refer :all]))

(def type-name "supermarket")
(def type-name-updated "grocery")

(describe "testing masterdata type CRUDs"
          (before-all
            (println "start testing...")
            (def type (create-masterdata-type-route {:type type-name})))
          (it "type should not be nil"
              (should-not (nil? type)))
          (it "type inside the lazy seq shouldn't also be nil"
              (should-not (nil? (first type))))
          (it "type should have an id"
              (should-not (nil? ((first type) :id))))
          (it "type should have a name"
              (should-not (nil? ((first type) :type))))
          (it "type should have the same name as the one it was created with"
              (should (= type-name ((first type) :type))))
          (it "type should have another name after its update"
              (update-masterdata-type-by-id ((first type) :id) {:type type-name-updated})
              (let [updated (read-masterdata-type-by-id ((first type) :id))]
                (should (= ((first updated) :type) type-name-updated))))
          (it "record should not exist anymore after its deletion"
              (let [new (create-masterdata-type-route {:type "todelete"})]
                (delete-masterdata-type-by-id ((first new) :id))
                (let [read (read-masterdata-type-by-id ((first new) :id))]
                  (should (nil? (first read))))))
          (after-all
            (delete-masterdata-type-by-id ((first type) :id))))
(run-specs)
