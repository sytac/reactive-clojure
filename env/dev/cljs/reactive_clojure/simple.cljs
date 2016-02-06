(ns reactive-clojure.simple
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.marbles :as marbles]
            [cljs.core.async :refer [put! chan <! onto-chan]]))

(def marbles (atom {:input [{:t 10 :l 1}
                            {:t 30 :l 2}
                            {:t 80 :l 3}]
                    :output []}))

(let [in  (chan)
      out (chan)]
  (onto-chan in (:input @marbles))
  (go (loop []
        (let [m (<! in)]
          (swap! marbles (fn [old]
                           (merge-with concat old {:output [m]}))))
        (recur))))

(defn simple-get []
  (sandbox/marble-sandbox
   (sandbox/sandbox "input" (marbles/marbles-box (map marbles/marble (:input @marbles))))
   (sandbox/operator "(<! channel)")
   (sandbox/sandbox "output" (marbles/marbles-box (map marbles/static-marble (:output @marbles))))))
