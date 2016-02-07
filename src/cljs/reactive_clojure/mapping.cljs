(ns reactive-clojure.mapping
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.marbles :as marbles]
            [reactive-clojure.utils :as utils]
            [cljs.core.async :refer [put! chan <! onto-chan pipe]]))

(def marbles (atom {:input [{:t 10 :l 1}
                            {:t 30 :l 2}
                            {:t 80 :l 3}]
                    :output []}))

(defn render []
  (let [input (chan)
        output (chan 1 (map (fn [{time :t label :l}] {:t time :l (inc label)})))
        _ (onto-chan input (:input @marbles))]
    (swap! marbles assoc :output [])
    (pipe input output)
    (utils/process output marbles)))

(render)

(defn mapping []
  (sandbox/marble-sandbox
   (sandbox/sandbox "input" (marbles/marbles-box (map (partial marbles/marble marbles render) (:input @marbles))))
   (sandbox/operator "(<! (chan 1 (map inc)))")
   (sandbox/sandbox "output" (marbles/marbles-box (map marbles/static-marble (:output @marbles))))))
