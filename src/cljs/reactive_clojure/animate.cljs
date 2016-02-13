(ns reactive-clojure.animate
  (:require [goog.events :as events])
  (:import [goog.events EventType]))

(defn id->box [id]
  (let [marble (.getElementById js/document id)
        box (.-parentElement marble)]
    box))

(defn box->xpos [box]
  (let [rect (.getBoundingClientRect box)]
    (.-left rect)))

(defn box->width [box]
  (let [rect (.getBoundingClientRect box)]
    (.-width rect)))

(defn find-marble [label store]
  (let [[idx el] (first (filter (fn [[idx el]] (= label (:l el)))
                                (map-indexed vector (:input @store))))]
    [idx el]))

(defn drag-move-fn [id label store render]
  (let [parent   (-> id id->box)
        parent-x (-> parent box->xpos)
        parent-w (-> parent box->width)]
    (fn [evt]
      (let [xpos (.-clientX evt)
            relpos (- xpos parent-x)
            newtime (max 0
                         (min 100 (/ (* relpos 100) parent-w)))
            [idx el] (find-marble label store)
            newinput (vec (sort-by :t (assoc-in (:input @store) [idx :t] newtime)))]
        (swap! store assoc :input newinput)
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
