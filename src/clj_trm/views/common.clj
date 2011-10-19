(ns clj-trm.views.common
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers)
  (:require [clj-trm.models.links :as links]
            [noir.session :as session]
            [noir.response :as resp])) 

(defpartial main-layout [& content]
  (html5
   [:head
    [:title "clj-trm"]
    (include-css "/css/reset.css")
    (include-css "/css/style.css")
    (include-js "/js/redirect.js")]
   [:body
    [:h1 "CLJ-TRM: the url shortener written in Clojure + Noir"]
    [:div#flash (session/flash-get)]
    [:div#wrapper
     content]]))

(defpartial shorten-fields []
  (form-to [:post "/trm"]
           (text-field {:placeholder "Address" :type "textbox"} :address)
           (submit-button {:class "submit"} "trm")))

(defpage "/" []
  (main-layout
            (shorten-fields)))

(defpage "/trm" {:keys [address base62]}
  (main-layout (when (and address base62)
                 [:p address " has been trm'd to " [:b "http://gentle-journey-8171.herokuapp.com/" base62]])
               (shorten-fields)))

(defpage [:post "/trm"] {address :address}
  (if
      (nil? (links/valid-html? address)) (do (session/flash-put!
                                              "Sorry, that doesn't look like a valid address")
                                             (resp/redirect "/"))
      (if-let [base62 (links/address->base62 address)]
        (render "/trm" {:address address :base62 base62})
        (do
          (links/add! address)
          (render "/trm" {:address address :base62 (links/address->base62 address)})))))
      

(defpage "/:trm" {:keys [trm]}
  (let [address (links/base62->address trm)]
    (if (nil? address)
      (do (session/flash-put! "Sorry, it doesn't look like that was a valid trm.")
          (resp/redirect "/"))
      (main-layout [:p "Redirecting you to " [:i address] " provided by clj-trm."
                    [:script {:type "text/javascript"} "trm_delay(\"" address "\");"]]))))