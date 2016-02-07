(ns reactive-clojure.utils
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [put! chan <! >! to-chan pipe]]))

(defn update-output [marbles elem]
  (swap! marbles (fn [old]
                   (merge-with into old {:output [elem]}))))

(defn process [c marbles]
  (go (loop []
        (let [m (<! c)]
          (when m
            (update-output marbles m)
            (recur))))))

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
