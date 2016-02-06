(ns reactive-clojure.cards
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [reactive-clojure.core :as core])
  (:require-macros
   [devcards.core
    :as dc
    :refer [defcard defcard-doc defcard-rg deftest]]))

(defcard-rg home-page-card
  [core/home-page])

(defn arrow []
  [:div
   [:div.diagramArrow {:style {:position "absolute"
                               :top "calc(48px)"
                               :height "2px"
                               :left "22px"
                               :right "22px"
                               :background-color "rgb(50, 50, 50)"}}]
   [:div.diagramArrowHead {:style {:width "0px"
                                   :height "0px"
                                   :border-top-width "8px"
                                   :border-top-style "solid"
                                   :border-top-color "transparent"
                                   :border-bottom-width "8px"
                                   :border-bottom-style "solid"
                                   :border-bottom-color "transparent"
                                   :border-left-width "calc(16px)"
                                   :border-left-style "solid"
                                   :border-left-color "rgb(50, 50, 50)"
                                   :display "inliine-block"
                                   :right "calc(21px)"
                                   :position "absolute"
                                   :top "calc(41px)"}}]])

(defn marble [draggable {time :t label :l}]
  [:div.marbleRoot.diagramMarble {:key (str draggable label)
                                  :style (merge {:display "inline-block"
                                                 :left (str time "%")
                                                 :position "relative"
                                                 :width "32px"
                                                 :height "32px"
                                                 :bottom "calc((100% - 87px) / 2)"
                                                 :margin "calc(-16px)"
                                                 :z-index time} (if draggable
                                                                  {:cursor "ew-resize"}
                                                                  {}))}
   [:svg {:viewBox "0 0 1 1"
          :style {:overflow "visible"
                  :width "32px"
                  :height "32px"}}
    [:circle {:cx "0.5"
              :cy "0.5"
              :r  "0.47"
              :stroke-width "0.06px"
              :style {:stroke "rgb(50, 50, 50)"
                      :fill "rgb(255, 105, 70)"}}]]
   [:p.marbleContent {:style {:-webkit-user-select "none"
                              :width "100%"
                              :height "100%"
                              :position "absolute"
                              :margin "0px"
                              :top "0px"
                              :text-align "center"
                              :line-height "32px"}} label]])

(defn marbles-box [marbles draggable]
  [:div {:style {:position "absolute"
                 :left "calc(70px)"
                 :right "calc(70px)"
                 :top "calc(48px)"
                 :height "44px"
                 :margin-top "calc(-22px)"}}
   (map (partial marble draggable) marbles)])

(defn sandbox [class marbles draggable]
  (let [element (keyword (str "div." class))]
    [element {:style {:-webkit-user-select "none"
                      :overflow "visible"
                      :display "block"
                      :width "100%"
                      :height "calc(96px)"
                      :position "relative"
                      :cursor "default"}}
     (arrow)
     (marbles-box marbles draggable)]))

(defn operator [text]
  [:div.operatorBox {:style {:border "1px solid rgba(0, 0, 0, 0.0588235)"
                             :padding "22px"
                             :text-align "center"
                             :position "relative"}}
   [:div {:style {:display "block"
                  :position "absolute"
                  :left "0px"
                  :top "0px"
                  :right "0px"
                  :bottom "0px"
                  :box-shadow "rgba(0, 0, 0, 0.168627) 0px 2px 10px 0px"}}]
   [:span.operatorLabel {:style {:font-family "'Source Code Pro', monospace"
                                 :font-weight "400"
                                 :font-size "2rem"}} text]
   [:div {:style {:display "block"
                  :position "absolute"
                  :left "0px"
                  :top "0px"
                  :right "0px"
                  :bottom "0px"
                  :box-shadow "rgba(0, 0, 0, 0.258824) 0px 2px 5px 0px"}}]])

(def marbles (atom
              [{:t 10
                :l 1}
               {:t 25
                :l 2}]))

(defcard-rg marble-card
  [:div {:style {:position "absolute"
                 :top "0px"
                 :display "inline-block"
                 :width "820px"}}
   [:div.sandboxRoot {:style {:width "820px"
                              :border-radius "2px"
                              :box-shadow "rgba(0, 0, 0, 0.168627) 0px 1px 2px 1px"
                              :background "rgb(255, 255, 255)"}}
    (sandbox "sandboxInputDiagram" @marbles true)
    (operator "findIndex(x => x > 10)")
    (sandbox "sandboxOutputDiagram" [{:t 20 :l 9}] false)]])

(reagent/render [:div] (.getElementById js/document "app"))

;; remember to run 'lein figwheel devcards' and then browse to
;; http://localhost:3449/cards
