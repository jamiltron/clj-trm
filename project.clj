(defproject clj-trm "0.0.1-SNAPSHOT"
            :description "A basic URL-shortener written with Noir (not done)"
            :dependencies [[org.clojure/clojure "1.3.0"]
                           [noir "1.2.0"]
                           [org.clojure/java.jdbc "0.0.7"]
                           [postgresql/postgresql "8.4-702.jdbc4"]]
            :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
            :main clj-trm.server)

