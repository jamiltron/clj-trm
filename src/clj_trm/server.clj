(ns clj-trm.server
  (:require [noir.server :as server]))

(server/load-views "src/clj_trm/views/")

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "5000"))]
    (server/start port {:mode mode
                        :ns 'clj-trm})))

