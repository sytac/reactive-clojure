(ns reactive-clojure.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [reactive-clojure.slow :as slow]))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to reactive-clojure"]
   [:div [:a {:href "/about"} "go to about page"]]
   [:div [:a {:href "/slow"} "slow counter"]]])

(defn about-page []
  [:div [:h2 "About reactive-clojure"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defn slow-page []
  [:div [:h2 "A counter with 1s delay"]
   [:div
    [:div.counter
     [:button.start-counter "start"]
     [:div.counter-value
      [:span (slow/counter-value)]]]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

(secretary/defroute "/slow" []
  (session/put! :current-page #'slow-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
