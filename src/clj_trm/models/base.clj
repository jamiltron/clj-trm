(ns clj-trm.models.base
  (:require [clojure.string :as string]
            [clojure.java.jdbc :as sql])
  (:import (java.net URI)))

(defn database-resource []
  (let [url (URI. (System/getenv "CLJTRM_DB"))
        host (.getHost url)
        port (if (pos? (.getPort url)) (.getPort url)
                 5432)
        path (.getPath url)]
    (merge
     {:subname (str "//" host ":" port path)}
     (if-let [user-info (.getUserInfo url)]
       {:user (first (string/split user-info #":"))
        :password (second (string/split user-info #":"))}))))

(def db
     (merge
      {:classname "org.postgresql.Driver"
       :subprotocol "postgresql"}
      (database-resource)))
           