(ns reactive-clojure.distinct
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.marbles :as marbles]
            [cljs.core.async :refer [put! chan <! >! onto-chan pipe]]))

(def marbles (atom {:input [{:t 10 :l 1}
                            {:t 30 :l 2}
                            {:t 40 :l 2}
                            {:t 50 :l 3}
                            {:t 80 :l 4}
                            {:t 90 :l 2}]
                    :output []}))

(defn update-output [elem]
  (.log js/console (str "Updating with " elem))
  (swap! marbles (fn [old]
                   (merge-with into old {:output [elem]}))))

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

(defn render []
  (let [input (chan)
        output (chan 1 (distinct-label))
        _ (onto-chan input (:input @marbles))]
    (swap! marbles assoc :output [])
    (pipe input output)
    (go (loop []
          (let [m (<! output)]
            (when m
              (update-output m)
              (recur)))))))

(render)

(defn distinct-xf []
  (let [in  (:input @marbles)
        out (:output @marbles)]
    (sandbox/marble-sandbox
     (sandbox/sandbox "input" (marbles/marbles-box
                               (map (partial marbles/marble marbles render) in)))
     (sandbox/operator "(<! (chan 1 (distinct)))")
     (sandbox/sandbox "output" (marbles/marbles-box
                                (map marbles/static-marble out))))))
