(ns reactive-clojure.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [reactive-clojure.middleware :refer [wrap-middleware]]
            [environ.core :refer [env]]
            [clojure.core.async :refer [go go-loop <! >! timeout alts! chan <!!]]
            [ring.util.anti-forgery :as crsf]))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(defn loading-page [req]
  (html
   [:html
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css (if (env :dev) "css/site.css" "css/site.min.css"))]
    [:body
     mount-target
     (crsf/anti-forgery-field)
     (include-js "js/app.js")]]))

(def cards-page
  (html
   [:html
    [:head
     [:meta {:charset "utf-8"}]]
    [:body
     mount-target
     (include-js "js/app_devcards.js")]]))

(defroutes routes
  (GET "/" [] loading-page)
  (GET "/about" [] loading-page)
  (GET "/cards" [] cards-page)
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
