(ns reactive-clojure.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]))

;; -------------------------
;; Views

(defn home-page []
  [:div {:style {:padding-left "10px"
                 :padding-right "52px"}}
   [:div {:style {:position "relative"
                  :width "1060px"
                  :margin "0px auto"}}
    [:h1 {:style {:font-family "Signika, Helvetica, serif"
                  :color "rgb(124, 124, 124)"
                  :display "inline-block"
                  :width "218px"}} "RxClojure"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
