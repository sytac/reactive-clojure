(ns reactive-clojure.distinct
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.marbles :as marbles]
            [cljs.core.async :refer [put! chan <! >! onto-chan]]))

(def marbles (atom {:input [{:t 10 :l 1}
                            {:t 30 :l 2}
                            {:t 40 :l 2}
                            {:t 50 :l 3}
                            {:t 80 :l 4}
                            {:t 90 :l 2}]
                    :output []}))

(defn render []
  (let [input (chan)
        output (chan 1 (distinct))
        _ (onto-chan input (:input @marbles))]
    (swap! marbles assoc :output [])
    (go (loop []
          (let [m (<! input)]
            (when m
              (>! output m)
              (recur)))))

    (go (loop []
          (let [m (<! output)]
            (.log js/console (str "got " m))
            (swap! marbles (fn [old]
                             (merge-with into old {:output [m]})))
            (recur))))))

(render)

(defn distinct-xf []
  (let [in  (:input @marbles)
        out (:output @marbles)]
    (sandbox/marble-sandbox
     (sandbox/sandbox "input" (marbles/marbles-box
                               (map (partial marbles/marble marbles render) in)))
     (sandbox/operator "(<! (chan 1 (distinct)))")
     (.log js/console (str out))
     (sandbox/sandbox "output" (marbles/marbles-box
                                (map marbles/static-marble out))))))
