(ns clj-trm.models.migration
  (:use [clj-trm.models.base :only (db)])
  (:require [clojure.java.jdbc :as sql]))

(defn create-db []
  (sql/with-connection db
    (sql/create-table :links
                      [:id :serial "PRIMARY KEY"]
                      [:address :varchar "NOT NULL"]
                      [:base62 :varchar])))

(defn -main []
  (print "Migrating database...")
  (flush)
  (create-db)
  (println "done!"))