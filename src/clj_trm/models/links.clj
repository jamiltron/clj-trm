(ns clj-trm.models.links
  (:use [clj-trm.models.base :only (db)])
  (:require [clojure.string :as string]
            [clojure.java.jdbc :as sql]
            [clj-trm.models.links :as links]))

(def base62-alphabet
  "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

; taken from daringfireball.net
(def html-re
  (re-pattern
   "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))"))

(defn valid-html? [address]
  (some #(= address %) (re-find html-re address)))

(defn base10->base62 [x]
  (letfn [(base-conv [curr q]
            (if (>= 0 q) (apply str curr)
                (recur (conj curr (nth base62-alphabet (rem q 62))) (quot q 62))))]
    (base-conv '() x)))

(defn single-select
  "Given a 'select' item and a 'where' item with the corresponding where value, return
  the 'select' value from the database, nil otherwise."
  [select-item where-item where-value]
  (let  [query (str "SELECT " select-item " FROM links WHERE " where-item "=?")]
  (sql/with-connection db
    (sql/with-query-results results
      [query where-value]
      ((keyword select-item) (first results))))))

(defn base62->address [base62]
  (single-select "address" "base62" base62))

(defn address->base62 [address]
  (single-select "base62" "address" address))

(defn address->id [address]
  (single-select "id" "address" address))

(defn add! [address]
  (sql/with-connection db
    (sql/insert-values :links [:address] [address])
    (sql/with-query-results results
      ["SELECT id FROM links WHERE address=?" address]
      (sql/update-values :links ["id=?" (:id (first results))]
                         {:base62 (base10->base62 (:id (first results)))}))))
             