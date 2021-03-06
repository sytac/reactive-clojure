> We need to shift the paradigm from reactive technologies to more integrative solutions that deal with the variety and complexity of the threats that are out there today.
> -- _John W. Thompson_

# Reactive clojure

[Reactive Extensions][rx] established a straightforward idiom for developers
that enables them to apply asynchronous programming patterns without the hassle
of callbacks hell or coordination overhead.

In the land of [Clojure][clj], while [some wrappers][rx-clj] are provided around
the Java Rx port, a more idiomatic approach to asynchronous programming is
provided by [`core.async`][async].

This repository provides examples of how to use `core.async` to accomplish
the same goals of some well known Rx patterns.

# Usage

To run the samples, you need to have [leiningen][lein]:

```
$ lein fighweel devcards
```

You can then visit <http://localhost:3449/cards#!/reactive_clojure.cards> to
see the samples in action.

# Credits

Next to technologies mentioned above, the following tools made this effort
possible:

- [`reagent`](https://github.com/reagent-project/reagent)
- [`reagint-template`](https://github.com/reagent-project/reagent-template)
- [`devcards`](https://github.com/bhauman/devcards)

[rx]: http://reactivex.io/
[clj]: http://clojure.org/
[rx-clj]: https://github.com/ReactiveX/RxClojure/
[async]: https://github.com/clojure/core.async
[lein]: http://leiningen.org
