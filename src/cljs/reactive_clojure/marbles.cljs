(ns reactive-clojure.marbles
  (:require [goog.events :as events])
  (:import [goog.events EventType]))

(defn id->box [id]
  (let [marble (.getElementById js/document id)
        box (.-parentElement (.-parentElement marble))]
    box))

(defn box->xpos [box]
  (let [rect (.getBoundingClientRect box)]
    (.-left rect)))

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

(defn find-marble [label store]
  (let [[idx el] (first (filter (fn [[idx el]] (= label (:l el)))
                                (map-indexed vector (:input @store))))]
    [idx el]))

(defn drag-move-fn [id label store render]
  (let [parent-x (-> id id->box box->xpos)]
    (fn [evt]
      (let [xpos (.-clientX evt)
            relpos (- xpos parent-x)
            [idx el] (find-marble label store)]
        (swap! store assoc-in [:input idx :t] relpos)
        (render)))))

(defn drag-end-fn [drag-move drag-end]
  (fn [evt]
    (events/unlisten js/window EventType.MOUSEMOVE drag-move)
    (events/unlisten js/window EventType.MOUSEUP @drag-end)))

(defn dragging [id label store render]
  (let [drag-move (drag-move-fn id label store render)
        drag-end-atom (atom nil)
        drag-end (drag-end-fn drag-move drag-end-atom)]
    (reset! drag-end-atom drag-end)
    (events/listen js/window EventType.MOUSEMOVE drag-move)
    (events/listen js/window EventType.MOUSEUP drag-end)))

(defn marble [store render {time :t label :l}]
  (let [id (gensym)]
    [:div.marbleRoot.diagramMarble {:id id
                                    :key label
                                    :on-mouse-down #(dragging id label store render)
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
