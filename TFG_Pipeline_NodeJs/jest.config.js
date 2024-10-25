module.exports = {
    testRunner: "jest-jasmine2",
    setupFilesAfterEnv: ["jest-allure/dist/setup"],
    testEnvironment: "node",
    collectCoverage: true,
    coverageReporters: ["lcov", "text"],
    coverageDirectory: "coverage",
    reporters: [
        "default",
        [
            "jest-allure",
            {
                resultsDir: "allure-results"
            }
        ]
    ]
};
