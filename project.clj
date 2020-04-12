(defproject jinx "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [cheshire "5.9.0"]
                 [clj-http "3.10.0"]
                 [com.fasterxml.jackson.dataformat/jackson-dataformat-yaml "2.9.2"]]
  :repl-options {:init-ns jinx.core}
  :test-selectors {:default (complement :integration)

                   :flaky :flaky

                   :only-name (fn [v & words]
                                (some (fn [word]
                                        (clojure.string/includes? (str (:name v))
                                                                  (str word)))
                                      words))

                   :print-ns-meta [(fn [n & _]
                                     (prn (type n) n)
                                     true)
                                   (constantly true)]

                   :print-var-meta (fn [m & _]
                                     (prn m)
                                     true)

                   :integration [(fn [ns & selector-args]
                                   (or (empty? selector-args)
                                       (some (fn [selector-arg]
                                               (-> selector-arg
                                                   str
                                                   (clojure.string/split #"/" 2)
                                                   first
                                                   symbol
                                                   (= ns)))
                                             selector-args)))
                                 (fn [m & selector-args]
                                   (when (:integration m)
                                     (or (empty? selector-args)
                                         (some (fn [selector-arg]
                                                 (let [a-var (str "#'" selector-arg)]
                                                   (if (some #{\/} a-var)
                                                     (= a-var (-> m :leiningen.test/var str))
                                                     (= selector-arg (ns-name (:ns m))))))
                                               selector-args))))]

                   :integration-simple (fn [m & selector-args]
                                         (when (:integration m)
                                           (or (empty? selector-args)
                                               (some (fn [selector-arg]
                                                       (let [a-var (str "#'" selector-arg)]
                                                         (if (some #{\/} a-var)
                                                           (= a-var (-> m :leiningen.test/var str))
                                                           (= selector-arg (ns-name (:ns m))))))
                                                     selector-args))))})
