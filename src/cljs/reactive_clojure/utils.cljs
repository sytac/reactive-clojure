(ns reactive-clojure.utils
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [put! chan <! >! to-chan pipe]]))

(defn update-output [marbles elem]
  (swap! marbles (fn [old]
                   (merge-with (comp vec
                                     (partial sort-by (comp identity :t))
                                     into) old {:output [elem]}))))

(defn process [c marbles]
  (let [state (atom {:output []})]
    (go (loop []
          (let [m (<! c)]
            (if (not (nil? m))
              (do
                (update-output state m)
                (recur))
              (swap! marbles merge @state)))))))

(defn distinct-label
  ([]
   (fn [rf]
     (let [seen (volatile! #{})]
       (fn
         ([] (rf))
         ([result] (rf result))
         ([result {_ :t label :l :as input}]
          (if (contains? @seen label)
            result
            (do (vswap! seen conj label)
                (rf result input)))))))))
