# Test Automation Plan for List Breweries API

## Approach
- Use Java + REST Assured + JUnit5
- Cover main query parameters: pagination (`page`, `per_page`), filtering (`by_city`, `by_state`, `by_type`, etc.), sorting (`asc`, `desc`).
- Validate status codes, response schema, and business rules.

## Test Design Techniques
- Equivalence Partitioning (valid/invalid pagination values).
- Boundary Values (per_page = 1, per_page = max).
- Negative Testing (invalid type, invalid query name).
- Data-driven testing (multiple states/cities).

## Example Scenarios
1. Default request without params → returns welcome message.
2. Pagination: page=2 returns different data than page=1.
3. Filtering by city/state/type returns only matching breweries.
4. Invalid params (page=-1, per_page=0, type=unknown) → welcome message.
5. Combination parameters (filtering + pagination) narrow down results.

## Estimated Effort
- 1-2 days for basic test implementation (setup, positive scenarios).
- +1 day for edge cases, negative scenarios.
- +1 day for CI/CD integration, reporting (e.g., Allure).