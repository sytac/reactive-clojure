(ns reactive-clojure.cards
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [reactive-clojure.core :as core]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.simple :as simple]
            [reactive-clojure.merge :as merge])
  (:require-macros
   [devcards.core
    :as dc
    :refer [defcard defcard-doc defcard-rg deftest]]))

(defcard-rg Intro
  [:div
   [:h1 "Reactive clojure with core.async"]
   [:p "Marbles diagrams for " [:code "core.async "] "operators"]])

(def marbles (atom
              [{:t 10
                :l 1}
               {:t 25
                :l 2}]))

(defcard-rg simple-get
  [:div
   (simple/simple-get)])

(defcard-rg merged-chans
  (merge/merged))

(reagent/render [:div] (.getElementById js/document "app"))

;; remember to run 'lein figwheel devcards' and then browse to
;; http://localhost:3449/cards
