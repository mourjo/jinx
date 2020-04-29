(defproject jinx "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [cheshire "5.9.0"]
                 [clj-http "3.10.0"]
                 [com.fasterxml.jackson.dataformat/jackson-dataformat-yaml "2.9.2"]
                 [ring/ring-jetty-adapter "1.8.0"]
                 [compojure "1.6.1"]]
  :repl-options {:init-ns jinx.core}
  :jvm-opts ["-Dorg.eclipse.jetty.LEVEL=OFF"]
  :test-selectors {:default (complement :integration)

                   :auxiliary :auxiliary

                   :critical (every-pred :integration (complement :flaky))

                   :flaky :flaky

                   :only-name (fn [var-metadata & words]
                                (some (fn [word]
                                        (clojure.string/includes? (str (:name var-metadata))
                                                                  (str word)))
                                      words))

                   :demo-ns-selector [(fn [namespc & _]
                                        (println (format "Should I select this namespace?\n%s\n\n" (str namespc)))
                                        true)
                                      (fn [var-metadata & _]
                                        (println (format "Should I select this var?\n%s\n\n" (str var-metadata)))
                                        true)]

                   :verbose (fn [var-metadata & _]
                              (prn var-metadata)
                              true)

                   :api-integration [(fn [namespc]
                                       ;; select namespaces that start with jinx.api.*
                                       (.startsWith (str namespc) "jinx.api."))

                                     ;; in those namespaces, select the tests marked
                                     ;; as ^:integration
                                     :integration]

                   :integration [(fn [namespc & selector-args]
                                   ;; Decide based on the command line arguments, whether the current
                                   ;; namespace's tests should be run or not based on whether the current
                                   ;; namespace is passed in as a command line argument or a var of the
                                   ;; namespace is passed in as a command line argument (or run
                                   ;; everything if nothing is passed). For this:
                                   ;; lein test :integration jinx.core-test jinx.api.time-test/start-stop-server-test
                                   ;; only jinx.core-test and jinx.api.time-test namespaces, this
                                   ;; function will return true allowing only tests in these namespaces
                                   ;; to be considered for running

                                   (or (empty? selector-args)
                                       (some (fn [selector-arg]
                                               (-> selector-arg
                                                   str
                                                   (clojure.string/split #"/" 2)
                                                   first
                                                   symbol
                                                   (= namespc)))
                                             selector-args)))
                                 (fn [var-metadata & selector-args]
                                   ;; Given a test var's metadata, decide whether this test var should be
                                   ;; run or not. We look at the command line arguments and qualify only
                                   ;; those vars that are mentioned in the command line (or all if
                                   ;; nothing is mentioned).  Among those, only select the vars with the
                                   ;; :integration metadata.
                                   (when (:integration var-metadata)
                                     (or (empty? selector-args)
                                         (some (fn [selector-arg]
                                                 (let [a-var (str "#'" selector-arg)]
                                                   (if (some #{\/} a-var)
                                                     (= a-var (-> var-metadata :leiningen.test/var str))
                                                     (= selector-arg (ns-name (:ns var-metadata))))))
                                               selector-args))))]

                   :integration-simple (fn [var-metadata & selector-args]
                                         (when (:integration var-metadata)
                                           (or (empty? selector-args)
                                               (some (fn [selector-arg]
                                                       (let [a-var (str "#'" selector-arg)]
                                                         (if (some #{\/} a-var)
                                                           (= a-var (-> var-metadata :leiningen.test/var str))
                                                           (= selector-arg (ns-name (:ns var-metadata))))))
                                                     selector-args))))})
