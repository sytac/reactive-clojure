(ns reactive-clojure.marbles
  (:require [reactive-clojure.animate :as anim]))

(defn color [label]
  (let [colors ["#FFFFFF" ; white
                "#ECECEC" ; almost white
                "#A7A7A7" ; grey
                "#7C7C7C" ; dark grey
                "#A7A732" ; black
                "#3EA1CB" ; blue
                "#FFCB46" ; yellow
                "#FF6946" ; red
                "#82D736" ; green
                ]
        code (.charCodeAt (str label) 0)]
    (colors (mod code (count colors)))))

(defn marble [store render {time :t label :l}]
  (let [id (str (gensym) "-marble")]
    [:div.marbleRoot.diagramMarble {:id id
                                    :key (str (gensym) "-" label)
                                    :on-mouse-down #(anim/dragging id label store render)
                                    :style {:display "inline-block"
                                            :left (str time "%")
                                            :position "relative"
                                            :cursor "ew-resize"
                                            :width "32px"
                                            :height "32px"
                                            :bottom "calc((100% - 87px) / 2)"
                                            :margin "calc(-16px)"
                                            :z-index time}}
     [:svg {:viewBox "0 0 1 1"
            :style {:overflow "visible"
                    :width "32px"
                    :height "32px"}}
      [:circle {:cx "0.5"
                :cy "0.5"
                :r  "0.47"
                :stroke-width "0.06px"
                :style {:stroke "rgb(50, 50, 50)"
                        :fill (color label)}}]]
     [:p.marbleContent {:style {:-webkit-user-select "none"
                                :width "100%"
                                :height "100%"
                                :position "absolute"
                                :margin "0px"
                                :top "0px"
                                :text-align "center"
                                :line-height "32px"}} label]]))

(defn static-marble [{time :t label :l}]
  [:div.marbleRoot.diagramMarble {:key (str (gensym) "-" label)
                                  :style {:display "inline-block"
                                          :left (str time "%")
                                          :position "relative"
                                          :width "32px"
                                          :height "32px"
                                          :bottom "calc((100% - 87px) / 2)"
                                          :margin "calc(-16px)"
                                          :z-index time}}
   [:svg {:viewBox "0 0 1 1"
          :style {:overflow "visible"
                  :width "32px"
                  :height "32px"}}
    [:circle {:cx "0.5"
              :cy "0.5"
              :r  "0.47"
              :stroke-width "0.06px"
              :style {:stroke "rgb(50, 50, 50)"
                      :fill (color label)}}]]
   [:p.marbleContent {:style {:-webkit-user-select "none"
                              :width "100%"
                              :height "100%"
                              :position "absolute"
                              :margin "0px"
                              :top "0px"
                              :text-align "center"
                              :line-height "32px"}} label]])

(defn marbles-box [& content]
  [:div.marbles-box {:style {:position "absolute"
                             :left "calc(70px)"
                             :right "calc(70px)"
                             :top "calc(48px)"
                             :height "44px"
                             :margin-top "calc(-22px)"}}
   content])
