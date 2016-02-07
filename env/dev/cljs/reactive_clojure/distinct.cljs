(ns reactive-clojure.distinct
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.marbles :as marbles]
            [reactive-clojure.utils :as utils]
            [cljs.core.async :refer [put! chan <! >! to-chan pipe]]))

(def marbles (atom {:input [{:t 10 :l 1}
                            {:t 30 :l 2}
                            {:t 40 :l 2}
                            {:t 50 :l 3}
                            {:t 80 :l 4}
                            {:t 90 :l 2}]
                    :output []}))

(defn render []
  (let [input (to-chan (:input @marbles))
        output (chan 1 (utils/distinct-label))]
    (swap! marbles assoc :output [])
    (pipe input output)
    (utils/process output marbles)))

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
