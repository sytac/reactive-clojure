(ns reactive-clojure.first
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.marbles :as marbles]
            [cljs.core.async :refer [put! chan <! onto-chan pipe close!]]))

(def marbles (atom {:input [{:t 10 :l 1}
                            {:t 30 :l 2}
                            {:t 80 :l 3}]
                    :output []}))

(defn update-output [elem]
  (swap! marbles (fn [old]
                   (merge-with into old {:output [elem]}))))

(defn render []
  (let [input (chan)
        output (chan)
        _ (onto-chan input (:input @marbles))]
    (swap! marbles assoc :output [])
    (pipe input output)
    (go
      (let [m (<! output)]
        (if m
          (update-output m)
          (close! m))))))

(render)

(defn just-first []
  (sandbox/marble-sandbox
   (sandbox/sandbox "input" (marbles/marbles-box (map (partial marbles/marble marbles render) (:input @marbles))))
   (sandbox/operator "(<! (chan 5))")
   (sandbox/sandbox "output" (marbles/marbles-box (map marbles/static-marble (:output @marbles))))))
