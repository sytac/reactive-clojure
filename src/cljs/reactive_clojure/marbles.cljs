(ns reactive-clojure.marbles)

(defn marble [{time :t label :l}]
  [:div.marbleRoot.diagramMarble {:key label
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
                      :fill "rgb(255, 105, 70)"}}]]
   [:p.marbleContent {:style {:-webkit-user-select "none"
                              :width "100%"
                              :height "100%"
                              :position "absolute"
                              :margin "0px"
                              :top "0px"
                              :text-align "center"
                              :line-height "32px"}} label]])

(defn static-marble [{time :t label :l}]
  [:div.marbleRoot.diagramMarble {:key label
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
                      :fill "rgb(255, 105, 70)"}}]]
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
