(ns reactive-clojure.marbles-sandbox)

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

(defn operator [text]
  [:div.operatorBox {:key (gensym)
                     :style {:border "1px solid rgba(0, 0, 0, 0.0588235)"
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

(defn sandbox [class content]
  (let [element (keyword (str "div." class "Sandbox"))]
    [element {:key (gensym)
              :style {:-webkit-user-select "none"
                      :overflow "visible"
                      :display "block"
                      :width "100%"
                      :height "calc(96px)"
                      :position "relative"
                      :cursor "default"}}
     (arrow)
     content]))

(defn marble-sandbox [& content]
  [:div {:style {:position "absolute"
                 :top "0px"
                 :display "inline-block"
                 :width "820px"}}
   [:div.sandboxRoot {:style {:width "820px"
                              :border-radius "2px"
                              :box-shadow "rgba(0, 0, 0, 0.168627) 0px 1px 2px 1px"
                              :background "rgb(255, 255, 255)"}}
    content]])
