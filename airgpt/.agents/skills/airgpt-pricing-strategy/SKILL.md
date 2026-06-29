---
name: airgpt-pricing-strategy
description: "Generate AirGPT short-term rental pricing strategy reports from internal group and competitor report MCP tools. Use when the user asks for a pricing strategy, AirGPT report, STR revenue recommendation, competitor-informed pricing calendar, or pricing rules for an internalGroupId in the Maltese short-term rental market."
---

# AirGPT Pricing Strategy

## Mission

Generate a data-driven short-term rental pricing strategy for the requested internal group. Maximize net revenue while keeping a competitive local market position by balancing occupancy and ADR to optimize RevPAR.

Default task when the user invokes this skill without a more specific prompt:

Deliver a pricing strategy for the provided internalGroupId based on the information, rules, and insights available in the internal group's reports. These reports contain pricing constraints, listing status, occupancy, rates, rules, and other relevant context.

Never begin the analysis without an `internalGroupId`. If the user does not provide one, ask whether to use the default internalGroupId `1bedroom-very-high-finish-very-high-amenities-side-sea-view-gzira`.

## Tool Workflow

Always create an AirGPT conversation identifier before any other AirGPT MCP tool call if no conversation ID is currently available in the conversation context. Use the available MCP tool named `generateConversationId` for this exact purpose. Do not call or rely on the old REST start/continue conversation APIs. Store the returned UUID and pass that exact UUID string as `conversationId` to every later AirGPT MCP tool call where necessary.

Expected AirGPT MCP capabilities include:

- A conversation-ID creation tool.
- `getCurrentDateTime(conversationId)` or equivalent current-time tool.
- `getCompetitorMetadata(conversationId)` - Provides a summary of the available competitor groups, mapping a competitor group ID to its metadata. For each group, provides metadata including the level of finish, amenities view etc, and also gives a list of all competitor properties in each group, together with a title and URL for each.
- `getConsumerGroupAvailableReportDays(conversationId, competitorGroupId)` - Returns the days when a report is available for the provided competitor group .
- `getCompetitorGroupReport(conversationId, competitorGroupId, reportGenerationDate)` - Retrieves the competitor group report for a specific date, including both an aggregate report across competitor properties in the same group, as well as a more detailed report per competitor property including calendar availability.
- `getWebsite(conversationId, url)` when event or market context is needed and the tool is available.

Use tool descriptions over names when names differ. Keep using the same generated conversation ID for all report discovery, report retrieval, website, and current-time calls in the current task whenever required as a tool call parameter.

## Decision Flow

1. If `internalGroupId` is missing, ask if the default value should be used, or if a different ID is to be used.
2. Retrieve the current UTC date/time so all horizons are anchored to tool-provided "today". Generate a conversation ID if not available.
3. Retrieve competitor metadata and select a broad but relevant comparison set.
4. Analyse the provided competitor meta minimum allowed net rate, cleaning fee, listing status, listing age, occupancy, existing calendar, current prices, current rules, and booking constraints.
5. Retrieve report availability days for selected groups, then retrieve multiple reports per selected group across different report-generation dates to identify pricing and availability trends over time.
6. Normalize competitor data before using it for pricing. Remove extreme high or low outliers. Flag suspicious or unreliable groups, and ignore groups with sustained 100% occupancy over many months as likely offline.
7. Assign sensible weights to competitor groups. Give greatest weight to similar bedroom count in Gzira and Sliema, especially similar finish, amenities, view, and proximity to seafront. Use other bedroom counts, localities, views, finish levels, pools, terraces, and premium amenities as lower-weight boundary signals.
8. Decide whether more context is needed or whether enough evidence exists to generate the pricing strategy. If competitor or market data is incomplete, continue with internal historical performance, seasonality, and the pricing framework rather than blocking, unless a required internal pricing constraint is missing.

Maintain an internal `assistantContext` as a concise markdown work log containing:

- A checklist of required information, with retrieved items marked `(Done)`.
- A list of information accrued from tools and user messages.
- A concise rationale including 
  - Data sources consulted
  - Key assumptions
  - Competitor groups selected and their weights
  - Pricing constraints applied
  - Calculation summaries
  - Confidence level
  - Any other strategies and ideas applied to come up with the final proposal.

You are free to apply any further expert level STR pricing strategy analysis, as long as this rationale is clearly documented within the `assistantContext`.

## Competitor normalization

- When sufficient observations exist (30 or more), remove extreme values outside two standard deviations.
- For smaller samples, prefer median and interquartile range (IQR) analysis, treating values outside 1.5×IQR as potential outliers.
- Always apply human judgment before excluding competitors, especially where premium amenities such as pools, exceptional views, or terraces may legitimately justify higher prices.

## Confidence level

Possible values:
- High
- Medium
- Low

Justify confidence based on:
- Number of comparable competitors
- Number of historical snapshots
- Market completeness
- Event information availability
- Internal performance history


## Required Strategy Coverage

The generated strategy must cover:

- Immediate horizon pricing for the next 7 days, per day.
- Short-term horizon pricing from day 7 through day 14 from today.
- Medium-term suggestions for one month from today and two months from today.
- Rule-based discount percentages, if any, for minimum-stay discounts selected from 3-day, 5-day, 10-day, 14-day, and 28-day stays. It is not required to apply all discount types.
- Last-minute booking discounts, including discount percentage and how far into the future they apply.
- A daily pricing calendar covering the next 4 months from today.
- Current performance analysis when the internal group is accepting bookings, factoring in seasonality and listing age.
- A bottom summary justifying the proposed strategy and factoring in listing performance.
- A bottom summary of competitor trends, including unusual price spikes, low or high demand ranges, low or high price ranges, other notable trends, and competitor groups with suspicious or unreliable information.

## Four-Month Calendar Columns

In the `assistantMessage`, include a markdown table for the next 4 months with one row per date. Include these columns:

- Date.
- Day of week.
- Suggested minimum stay duration if a guest books on that date.
- Conservative net price, excluding Airbnb service fee and amortized cleaning fee.
- Conservative gross price, including the 18.34% Airbnb service fee and amortized cleaning fee based on suggested minimum stay.
- Brief conservative justification and applied logic.
- Conservative net price after applying suggested duration-based and last-minute discounts.
- Balanced net price, excluding Airbnb service fee and amortized cleaning fee.
- Balanced gross price, including the 18.34% Airbnb service fee and amortized cleaning fee based on suggested minimum stay.
- Brief balanced justification and applied logic.
- Balanced net price after applying suggested duration-based and last-minute discounts.
- Aggressive net price, excluding Airbnb service fee and amortized cleaning fee.
- Aggressive gross price, including the 18.34% Airbnb service fee and amortized cleaning fee based on suggested minimum stay.
- Brief aggressive justification and applied logic.
- Aggressive net price after applying suggested duration-based and last-minute discounts.
- AirGPT recommendation for which model to use on that day.

## Pricing Models

Use these definitions exactly:

- Conservative: prioritize occupancy, even at the cost of suppressed rates.
- Balanced: maximize ADR while seeking an optimal balance between occupancy and daily rates.
- Aggressive: seek the highest daily rates, accepting the risk of suppressed occupancy.

## Pricing Rules

Use net prices as the primary recommendation. Net prices exclude Airbnb service fee and exclude amortized cleaning fee per day. Round suggested net daily rates to the nearest EUR 5 increment.

Respect the internal group's minimum allowed net rate extracted via `getCompetitorMetadata`. Neither base net prices nor discount-adjusted net prices may go below that minimum.

Competitor prices are inclusive of cleaning fees and Airbnb service fees. Account for this when comparing competitor prices to proposed internal net prices.

Calculate gross daily prices as:

```text
grossDailyPrice = (netDailyPrice * 1.1834) + (cleaningFee / suggestedMinimumStayNights)
```

When duration and last-minute discounts both apply, apply them consistently and make the convention clear. Prefer sequential multiplication unless the internal report defines a different stacking rule:

```text
discountedNet = max(minimumAllowedNetRate, netDailyPrice * (1 - durationDiscount) * (1 - lastMinuteDiscount))
```

Apply discounts only where the data and strategy support them. The pricing strategy should preserve a premium brand perception. Prefer maintaining strong base rates and using targeted discounts to create urgency and perceived value.

## Market Framework

Seasonality:

- Malta peak season runs from June through September.
- Shoulder seasons are April, May, and October.
- Low season is mid-January through March.
- The festive season around Christmas and New Year can briefly reach peak-season strength.

Locality:

- Premium Maltese areas include Sliema, Valletta, and St Julians.
- Gzira can approach premium positioning when close to the seafront.
- Floriana can also be premium.

Weekday pricing:

- Friday and Saturday are premium days.
- Thursday and Sunday are still expensive.
- Monday and Wednesday are cheaper.
- Tuesday is usually cheapest.

Strategies to apply when justified:

- Peak season boost: lift prices in peak season, taper in shoulder season, and lower in slow season.
- Length-based discounts: be stricter in high season and more open in low season, both in discount size and shorter qualifying stays.
- Last-minute discounts: hold rates longer in peak season; be more generous in slow season while preserving a quality signal.
- Minimum-stay duration: bias toward shorter minimum stays for visibility, never below 2 nights. Raise minimum stays around highly attractive periods such as Christmas week when revenue protection matters.
- Distance-date premium: charge guests more for booking far ahead when the calendar is open.
- Competitor-based market demand and pricing: use competitor price and availability trends to infer demand and target levels.
- Competitor boundaries: finish level, amenity level, view, pool, seafront proximity, locality, and terrace can materially shift achievable price.
- Competitor timeline trends: treat repeated availability drops and price spikes around dates as signals of enhanced demand.
- High-season strategy: Price to be booked after lower-quality competitors, while remaining sufficiently competitive to achieve strong occupancy before season end.
- Low-season strategy: price competitively to be booked relatively early.
- Adjacency discounts: suppress awkward open nights before an existing check-in or after an existing check-out, except on Friday and Saturday.
- Event-driven anomalies: exploit clear spikes around festivals, holidays, and major local events such as Isle of MTV.
- Search visibility: Search visibility is a first-class objective. Apply known knowledge around the Airbnb search algorithm to ensure this target.
- Rankings and reviews: When listings have few reviews, low booking velocity, or weak search positioning, temporarily favor occupancy and conversion over ADR optimization. Once review volume and ranking stabilize, gradually transition toward balanced or aggressive strategies. Early bookings and positive reviews are strategic assets whose long-term value may outweigh short-term ADR optimisation. 

## Response Contract

Return exactly one valid markdown report including:
- assistantMessage: Markdown-formatted human-readable response or error/question.
- assistantContext: Markdown-formatted checklist, accrued information, and any further rationale.

Keep narrative concise and focused, but include all required tables and summaries.
