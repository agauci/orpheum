# Pricing Strategy Report - 1bedroom-very-high-finish-very-high-amenities-side-sea-view-gzira - 2026-06-29 16:07 UTC

## Executive Recommendation

Use **Balanced** as the default pricing posture, with short tactical moves rather than broad discounting. The listing is open for bookings, is about eight months old, and has already filled the next 7 days, July 1-10, most of August, and several late-July dates. That is strong peak-season conversion for a premium Gzira one-bedroom, so the strategy should protect ADR while using conservative pricing only on the open July pockets that still need to convert.

Primary recommendation: keep net pricing premium but visible. July open dates should sit mostly at EUR 180-210 net before discounts. August should be materially higher, especially around the scarce August 19-22 gap, at EUR 215-245 net before discounts. September should taper back toward July levels. October should move into shoulder-season occupancy mode while preserving the premium brand signal.

## Pricing Models

- Conservative: prioritize occupancy, even at the cost of suppressed rates.
- Balanced: maximize ADR while seeking an optimal balance between occupancy and daily rates.
- Aggressive: seek the highest daily rates, accepting the risk of suppressed occupancy.

## Internal Listing Snapshot

| Field | Value |
|---|---|
| Internal group | `1bedroom-very-high-finish-very-high-amenities-side-sea-view-gzira` |
| Listing | Designer Penthouse by the Promenade / BBQ Terrace (`residenza-teatru`) |
| Location | Gzira, very close to seafront, side sea view |
| Positioning | 1 bedroom, very high finish, very high amenities, terrace and barbecue |
| Open for bookings | Yes |
| Launched | 2025-10-27 |
| Maximum occupancy | 4 guests |
| Minimum allowed net rate | EUR 90 |
| Cleaning fee | EUR 60 |
| Current AirGPT timestamp anchor | 2026-06-29 16:07 UTC |

## Current Performance Analysis

The listing has strong near-term occupancy: the internal report generated on 2026-06-28 showed no availability in the next 7 days, July 1-10 occupied/closed, July at 48% occupancy, and August at 84% occupancy. This is a healthy peak-season book for an eight-month-old premium listing. The main revenue risk is not low demand overall; it is over-discounting the remaining July and August gaps after the calendar already proved it can convert.

The open July pockets are July 11-18 and July 21-28. They remain the main conversion priority because they are close enough to arrival to affect search ranking and review velocity. The August inventory is scarce, especially the August 19-22 gap and August 31, so August should be priced closer to the premium comp ceiling and should not receive broad last-minute or length-of-stay discounts.

## Pricing Constraints and Formulae

- Net prices are the primary recommendation and exclude Airbnb guest service fee and amortized cleaning fee.
- Suggested base net prices are rounded to the nearest EUR 5.
- Base net prices and discount-adjusted net prices never go below the minimum allowed net rate of EUR 90.
- Competitor report prices were treated as gross/guest-facing prices that include Airbnb service fee and cleaning effects.
- Gross daily prices in the calendar use: `grossDailyPrice = (netDailyPrice * 1.1834) + (60 / suggestedMinimumStayNights)`.
- Discount stacking convention: `discountedNet = max(90, netDailyPrice * (1 - durationDiscount) * (1 - lastMinuteDiscount))`.

## Recommended Rules and Discounts

The current 15% last-minute discount is too blunt for peak season. Replace it with a smaller, more controlled urgency lever and reduce long-stay discounts during July-August.

| Rule | Recommended peak-season setting, June-September | Recommended October shoulder setting | Notes |
|---|---:|---:|---|
| Minimum stay | 2 nights most days; 3 nights Friday/Saturday starts in July-September; 4 nights only where it protects the August 19-22 gap | 2 nights most days | Keep visibility high; do not go below 2 nights. |
| 3+ night discount | 3% | 5% | Use as a modest conversion nudge, not a rate reset. |
| 5+ night discount | 5% | 7% | Useful for weekdays and shoulder periods. |
| 10+ night discount | 8% | 10% | Preserve ADR in July-August. |
| 14+ night discount | 10% | 12% | Do not allow below EUR 90 net after stacking. |
| 28+ night discount | 12% | 15% | Apply mainly for October and later low-demand periods. |
| Last-minute discount | 8% within 5 days only when a date is actually open and not a Friday/Saturday peak start | 10% within 7 days for open non-weekend starts | Avoid blanket last-minute discounting on scarce August dates. |
| Awkward-gap exception | Optional extra 5% inside 10 days for one- or two-night orphan gaps | Optional extra 5% inside 14 days | Use manually; do not automate across all dates. |

The daily calendar below applies the suggested minimum-stay discount for the row and applies the last-minute discount only to dates within 5 days of 2026-06-29. Longer-stay discounts should be applied only when the actual booking length qualifies.

## Competitor Signal Summary

| Signal group | Weight | Latest report dates used | Useful pricing signal | Treatment |
|---|---:|---|---|---|
| Internal listing history | 30% | 2026-06-15, 2026-06-21, 2026-06-28 | July gross moved from about EUR 246 to EUR 224 while July occupancy rose only from 42% to 48%; August occupancy rose to 84% | Strongest performance signal; use to protect August and tune July. |
| 1BR high finish/high amenities seafront Gzira | 25% | 2026-06-29 | EUR 233.50 near-term gross, EUR 239.67 July, EUR 292.23 August, with 39%-45% July/August occupancy | Direct premium ceiling, adjusted down for internal side view but up for very high finish and BBQ terrace. |
| Gzira terrace/penthouse medium group | 20% | 2026-06-15, 2026-06-21, 2026-06-28 | Valid terrace comps mostly cluster around EUR 180-254 gross for July/August, with some lower noisy entries | Used as practical local boundary. |
| 1BR medium/no-view close-to-seafront Gzira | 10% | 2026-06-15, 2026-06-21, 2026-06-28 | July/August around EUR 129-172 group gross, with one property fully booked in July | Lower-bound conversion signal; not a brand ceiling. |
| 1BR medium/no-view Gzira and 1BR high/no-view Sliema | 8% | 2026-06-28 | Gzira no-view July/August EUR 197/216; Sliema high no-view July/August EUR 192/226 | Confirms premium 1BR can hold EUR 190-225 gross even without the same amenity/view package. |
| Premium 2BR Gzira/Sliema boundaries | 7% | 2026-06-28 | Valid observations near EUR 246-315 gross; several EUR 0.x artifacts | Used only as an upper-bound caution, not a direct comp. |

## Competitor Normalization and Outliers

The sample is small, so I used median/IQR-style judgment rather than mechanical standard-deviation filtering. Values below EUR 50 gross were excluded as scraping or pricing artifacts, including EUR 0.28, EUR 0.34, EUR 0.36, EUR 0.39, EUR 0.40, and similar entries in the internal historical and Sliema boundary reports. Competitor groups with sustained 100% occupancy and no usable rates were treated as unreliable/offline signals, especially `refined-elegance` in the 2BR Gzira close-to-seafront group. Premium amenities such as terraces, barbecue, sea view, and seafront proximity were preserved as legitimate price drivers rather than treated as outliers.

## Immediate Horizon Pricing - Next 7 Days

| Date | Day | Status | Min stay | Conservative net | Balanced net | Aggressive net | Recommendation |
|---|---|---|---|---|---|---|---|
| 2026-06-29 | Monday | Booked/closed in latest report | 2 nights | EUR 165 | EUR 185 | EUR 210 | Booked; Balanced if reopened |
| 2026-06-30 | Tuesday | Booked/closed in latest report | 2 nights | EUR 165 | EUR 185 | EUR 210 | Booked; Balanced if reopened |
| 2026-07-01 | Wednesday | Booked/closed in latest report | 2 nights | EUR 165 | EUR 185 | EUR 210 | Booked; Balanced if reopened |
| 2026-07-02 | Thursday | Booked/closed in latest report | 2 nights | EUR 175 | EUR 195 | EUR 220 | Booked; Balanced if reopened |
| 2026-07-03 | Friday | Booked/closed in latest report | 3 nights | EUR 190 | EUR 210 | EUR 235 | Booked; Balanced if reopened |
| 2026-07-04 | Saturday | Booked/closed in latest report | 3 nights | EUR 190 | EUR 210 | EUR 235 | Booked; Balanced if reopened |
| 2026-07-05 | Sunday | Booked/closed in latest report | 2 nights | EUR 175 | EUR 195 | EUR 220 | Booked; Balanced if reopened |

## Short-Term Horizon Pricing - Day 7 Through Day 14

| Date | Day | Status | Min stay | Conservative net | Balanced net | Aggressive net | Recommendation |
|---|---|---|---|---|---|---|---|
| 2026-07-06 | Monday | Booked/closed in latest report | 2 nights | EUR 165 | EUR 185 | EUR 210 | Booked; Balanced if reopened |
| 2026-07-07 | Tuesday | Booked/closed in latest report | 2 nights | EUR 165 | EUR 185 | EUR 210 | Booked; Balanced if reopened |
| 2026-07-08 | Wednesday | Booked/closed in latest report | 2 nights | EUR 165 | EUR 185 | EUR 210 | Booked; Balanced if reopened |
| 2026-07-09 | Thursday | Booked/closed in latest report | 2 nights | EUR 175 | EUR 195 | EUR 220 | Booked; Balanced if reopened |
| 2026-07-10 | Friday | Booked/closed in latest report | 3 nights | EUR 190 | EUR 210 | EUR 235 | Booked; Balanced if reopened |
| 2026-07-11 | Saturday | Open in latest report | 3 nights | EUR 190 | EUR 210 | EUR 235 | Conservative |
| 2026-07-12 | Sunday | Open in latest report | 2 nights | EUR 175 | EUR 195 | EUR 220 | Conservative |
| 2026-07-13 | Monday | Open in latest report | 2 nights | EUR 160 | EUR 180 | EUR 205 | Conservative |

## Medium-Term Suggestions

| Horizon | Date | Day | Status | Min stay | Conservative net | Balanced net | Aggressive net | Recommendation |
|---|---|---|---|---|---|---|---|---|
| One month from today | 2026-07-29 | Wednesday | Booked/closed in latest report | 2 nights | EUR 160 | EUR 180 | EUR 205 | Booked; Balanced if reopened |
| Two months from today | 2026-08-29 | Saturday | Booked/closed in latest report | 3 nights | EUR 220 | EUR 245 | EUR 275 | Booked; Balanced if reopened |

Notes: 2026-07-29 and 2026-08-29 were shown as booked/closed in the latest internal report. If either date reopens because of a cancellation, use the listed fallback model rather than discounting below the premium comp set.

## Four-Month Daily Pricing Calendar

| Date | Day of week | Suggested minimum stay duration if a guest books on that date | Conservative net price, excluding Airbnb service fee and amortized cleaning fee | Conservative gross price, including the 18.34% Airbnb service fee and amortized cleaning fee based on suggested minimum stay | Brief conservative justification and applied logic | Conservative net price after applying suggested duration-based and last-minute discounts | Balanced net price, excluding Airbnb service fee and amortized cleaning fee | Balanced gross price, including the 18.34% Airbnb service fee and amortized cleaning fee based on suggested minimum stay | Brief balanced justification and applied logic | Balanced net price after applying suggested duration-based and last-minute discounts | Aggressive net price, excluding Airbnb service fee and amortized cleaning fee | Aggressive gross price, including the 18.34% Airbnb service fee and amortized cleaning fee based on suggested minimum stay | Brief aggressive justification and applied logic | Aggressive net price after applying suggested duration-based and last-minute discounts | AirGPT recommendation for which model to use on that day |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| 2026-06-29 | Monday | 2 nights | EUR 165 | EUR 225.26 | Booked; fallback only | EUR 152 | EUR 185 | EUR 248.93 | Hold quality if reopened | EUR 170 | EUR 210 | EUR 278.51 | Cancellation premium | EUR 193 | Booked; Balanced if reopened |
| 2026-06-30 | Tuesday | 2 nights | EUR 165 | EUR 225.26 | Booked; fallback only | EUR 152 | EUR 185 | EUR 248.93 | Hold quality if reopened | EUR 170 | EUR 210 | EUR 278.51 | Cancellation premium | EUR 193 | Booked; Balanced if reopened |
| 2026-07-01 | Wednesday | 2 nights | EUR 165 | EUR 225.26 | Booked; fallback only | EUR 152 | EUR 185 | EUR 248.93 | Hold quality if reopened | EUR 170 | EUR 210 | EUR 278.51 | Cancellation premium | EUR 193 | Booked; Balanced if reopened |
| 2026-07-02 | Thursday | 2 nights | EUR 175 | EUR 237.09 | Booked; fallback only | EUR 161 | EUR 195 | EUR 260.76 | Hold quality if reopened | EUR 179 | EUR 220 | EUR 290.35 | Cancellation premium | EUR 202 | Booked; Balanced if reopened |
| 2026-07-03 | Friday | 3 nights | EUR 190 | EUR 244.85 | Booked; fallback only | EUR 170 | EUR 210 | EUR 268.51 | Hold quality if reopened | EUR 187 | EUR 235 | EUR 298.10 | Cancellation premium | EUR 210 | Booked; Balanced if reopened |
| 2026-07-04 | Saturday | 3 nights | EUR 190 | EUR 244.85 | Booked; fallback only | EUR 170 | EUR 210 | EUR 268.51 | Hold quality if reopened | EUR 187 | EUR 235 | EUR 298.10 | Cancellation premium | EUR 210 | Booked; Balanced if reopened |
| 2026-07-05 | Sunday | 2 nights | EUR 175 | EUR 237.09 | Booked; fallback only | EUR 175 | EUR 195 | EUR 260.76 | Hold quality if reopened | EUR 195 | EUR 220 | EUR 290.35 | Cancellation premium | EUR 220 | Booked; Balanced if reopened |
| 2026-07-06 | Monday | 2 nights | EUR 165 | EUR 225.26 | Booked; fallback only | EUR 165 | EUR 185 | EUR 248.93 | Hold quality if reopened | EUR 185 | EUR 210 | EUR 278.51 | Cancellation premium | EUR 210 | Booked; Balanced if reopened |
| 2026-07-07 | Tuesday | 2 nights | EUR 165 | EUR 225.26 | Booked; fallback only | EUR 165 | EUR 185 | EUR 248.93 | Hold quality if reopened | EUR 185 | EUR 210 | EUR 278.51 | Cancellation premium | EUR 210 | Booked; Balanced if reopened |
| 2026-07-08 | Wednesday | 2 nights | EUR 165 | EUR 225.26 | Booked; fallback only | EUR 165 | EUR 185 | EUR 248.93 | Hold quality if reopened | EUR 185 | EUR 210 | EUR 278.51 | Cancellation premium | EUR 210 | Booked; Balanced if reopened |
| 2026-07-09 | Thursday | 2 nights | EUR 175 | EUR 237.09 | Booked; fallback only | EUR 175 | EUR 195 | EUR 260.76 | Hold quality if reopened | EUR 195 | EUR 220 | EUR 290.35 | Cancellation premium | EUR 220 | Booked; Balanced if reopened |
| 2026-07-10 | Friday | 3 nights | EUR 190 | EUR 244.85 | Booked; fallback only | EUR 184 | EUR 210 | EUR 268.51 | Hold quality if reopened | EUR 204 | EUR 235 | EUR 298.10 | Cancellation premium | EUR 228 | Booked; Balanced if reopened |
| 2026-07-11 | Saturday | 3 nights | EUR 190 | EUR 244.85 | Fill July gap | EUR 184 | EUR 210 | EUR 268.51 | Below prime comp ceiling | EUR 204 | EUR 235 | EUR 298.10 | Quality upside test | EUR 228 | Conservative |
| 2026-07-12 | Sunday | 2 nights | EUR 175 | EUR 237.09 | Fill July gap | EUR 175 | EUR 195 | EUR 260.76 | Below prime comp ceiling | EUR 195 | EUR 220 | EUR 290.35 | Quality upside test | EUR 220 | Conservative |
| 2026-07-13 | Monday | 2 nights | EUR 160 | EUR 219.34 | Fill July gap | EUR 160 | EUR 180 | EUR 243.01 | Below prime comp ceiling | EUR 180 | EUR 205 | EUR 272.60 | Quality upside test | EUR 205 | Conservative |
| 2026-07-14 | Tuesday | 2 nights | EUR 160 | EUR 219.34 | Fill July gap | EUR 160 | EUR 180 | EUR 243.01 | Below prime comp ceiling | EUR 180 | EUR 205 | EUR 272.60 | Quality upside test | EUR 205 | Balanced |
| 2026-07-15 | Wednesday | 2 nights | EUR 160 | EUR 219.34 | Fill July gap | EUR 160 | EUR 180 | EUR 243.01 | Below prime comp ceiling | EUR 180 | EUR 205 | EUR 272.60 | Quality upside test | EUR 205 | Balanced |
| 2026-07-16 | Thursday | 2 nights | EUR 175 | EUR 237.09 | Fill July gap | EUR 175 | EUR 195 | EUR 260.76 | Below prime comp ceiling | EUR 195 | EUR 220 | EUR 290.35 | Quality upside test | EUR 220 | Balanced |
| 2026-07-17 | Friday | 3 nights | EUR 190 | EUR 244.85 | Fill July gap | EUR 184 | EUR 210 | EUR 268.51 | Below prime comp ceiling | EUR 204 | EUR 235 | EUR 298.10 | Quality upside test | EUR 228 | Balanced |
| 2026-07-18 | Saturday | 3 nights | EUR 190 | EUR 244.85 | Fill July gap | EUR 184 | EUR 210 | EUR 268.51 | Below prime comp ceiling | EUR 204 | EUR 235 | EUR 298.10 | Quality upside test | EUR 228 | Balanced |
| 2026-07-19 | Sunday | 2 nights | EUR 175 | EUR 237.09 | Booked; fallback only | EUR 175 | EUR 195 | EUR 260.76 | Hold quality if reopened | EUR 195 | EUR 220 | EUR 290.35 | Cancellation premium | EUR 220 | Booked; Balanced if reopened |
| 2026-07-20 | Monday | 2 nights | EUR 160 | EUR 219.34 | Booked; fallback only | EUR 160 | EUR 180 | EUR 243.01 | Hold quality if reopened | EUR 180 | EUR 205 | EUR 272.60 | Cancellation premium | EUR 205 | Booked; Balanced if reopened |
| 2026-07-21 | Tuesday | 2 nights | EUR 160 | EUR 219.34 | Fill July gap | EUR 160 | EUR 180 | EUR 243.01 | Below prime comp ceiling | EUR 180 | EUR 205 | EUR 272.60 | Quality upside test | EUR 205 | Balanced |
| 2026-07-22 | Wednesday | 2 nights | EUR 160 | EUR 219.34 | Fill July gap | EUR 160 | EUR 180 | EUR 243.01 | Below prime comp ceiling | EUR 180 | EUR 205 | EUR 272.60 | Quality upside test | EUR 205 | Balanced |
| 2026-07-23 | Thursday | 2 nights | EUR 175 | EUR 237.09 | Fill July gap | EUR 175 | EUR 195 | EUR 260.76 | Below prime comp ceiling | EUR 195 | EUR 220 | EUR 290.35 | Quality upside test | EUR 220 | Balanced |
| 2026-07-24 | Friday | 3 nights | EUR 190 | EUR 244.85 | Fill July gap | EUR 184 | EUR 210 | EUR 268.51 | Below prime comp ceiling | EUR 204 | EUR 235 | EUR 298.10 | Quality upside test | EUR 228 | Balanced |
| 2026-07-25 | Saturday | 3 nights | EUR 190 | EUR 244.85 | Fill July gap | EUR 184 | EUR 210 | EUR 268.51 | Below prime comp ceiling | EUR 204 | EUR 235 | EUR 298.10 | Quality upside test | EUR 228 | Balanced |
| 2026-07-26 | Sunday | 2 nights | EUR 175 | EUR 237.09 | Fill July gap | EUR 175 | EUR 195 | EUR 260.76 | Below prime comp ceiling | EUR 195 | EUR 220 | EUR 290.35 | Quality upside test | EUR 220 | Balanced |
| 2026-07-27 | Monday | 2 nights | EUR 160 | EUR 219.34 | Fill July gap | EUR 160 | EUR 180 | EUR 243.01 | Below prime comp ceiling | EUR 180 | EUR 205 | EUR 272.60 | Quality upside test | EUR 205 | Conservative |
| 2026-07-28 | Tuesday | 2 nights | EUR 160 | EUR 219.34 | Fill July gap | EUR 160 | EUR 180 | EUR 243.01 | Below prime comp ceiling | EUR 180 | EUR 205 | EUR 272.60 | Quality upside test | EUR 205 | Conservative |
| 2026-07-29 | Wednesday | 2 nights | EUR 160 | EUR 219.34 | Booked; fallback only | EUR 160 | EUR 180 | EUR 243.01 | Hold quality if reopened | EUR 180 | EUR 205 | EUR 272.60 | Cancellation premium | EUR 205 | Booked; Balanced if reopened |
| 2026-07-30 | Thursday | 2 nights | EUR 175 | EUR 237.09 | Booked; fallback only | EUR 175 | EUR 195 | EUR 260.76 | Hold quality if reopened | EUR 195 | EUR 220 | EUR 290.35 | Cancellation premium | EUR 220 | Booked; Balanced if reopened |
| 2026-07-31 | Friday | 3 nights | EUR 190 | EUR 244.85 | Booked; fallback only | EUR 184 | EUR 210 | EUR 268.51 | Hold quality if reopened | EUR 204 | EUR 235 | EUR 298.10 | Cancellation premium | EUR 228 | Booked; Balanced if reopened |
| 2026-08-01 | Saturday | 3 nights | EUR 220 | EUR 280.35 | Booked; fallback only | EUR 213 | EUR 245 | EUR 309.93 | Hold quality if reopened | EUR 238 | EUR 275 | EUR 345.44 | Cancellation premium | EUR 267 | Booked; Balanced if reopened |
| 2026-08-02 | Sunday | 2 nights | EUR 205 | EUR 272.60 | Booked; fallback only | EUR 205 | EUR 230 | EUR 302.18 | Hold quality if reopened | EUR 230 | EUR 260 | EUR 337.68 | Cancellation premium | EUR 260 | Booked; Balanced if reopened |
| 2026-08-03 | Monday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-04 | Tuesday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-05 | Wednesday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-06 | Thursday | 2 nights | EUR 205 | EUR 272.60 | Booked; fallback only | EUR 205 | EUR 230 | EUR 302.18 | Hold quality if reopened | EUR 230 | EUR 260 | EUR 337.68 | Cancellation premium | EUR 260 | Booked; Balanced if reopened |
| 2026-08-07 | Friday | 3 nights | EUR 220 | EUR 280.35 | Booked; fallback only | EUR 213 | EUR 245 | EUR 309.93 | Hold quality if reopened | EUR 238 | EUR 275 | EUR 345.44 | Cancellation premium | EUR 267 | Booked; Balanced if reopened |
| 2026-08-08 | Saturday | 3 nights | EUR 220 | EUR 280.35 | Booked; fallback only | EUR 213 | EUR 245 | EUR 309.93 | Hold quality if reopened | EUR 238 | EUR 275 | EUR 345.44 | Cancellation premium | EUR 267 | Booked; Balanced if reopened |
| 2026-08-09 | Sunday | 2 nights | EUR 205 | EUR 272.60 | Booked; fallback only | EUR 205 | EUR 230 | EUR 302.18 | Hold quality if reopened | EUR 230 | EUR 260 | EUR 337.68 | Cancellation premium | EUR 260 | Booked; Balanced if reopened |
| 2026-08-10 | Monday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-11 | Tuesday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-12 | Wednesday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-13 | Thursday | 2 nights | EUR 205 | EUR 272.60 | Booked; fallback only | EUR 205 | EUR 230 | EUR 302.18 | Hold quality if reopened | EUR 230 | EUR 260 | EUR 337.68 | Cancellation premium | EUR 260 | Booked; Balanced if reopened |
| 2026-08-14 | Friday | 3 nights | EUR 220 | EUR 280.35 | Booked; fallback only | EUR 213 | EUR 245 | EUR 309.93 | Hold quality if reopened | EUR 238 | EUR 275 | EUR 345.44 | Cancellation premium | EUR 267 | Booked; Balanced if reopened |
| 2026-08-15 | Saturday | 3 nights | EUR 220 | EUR 280.35 | Booked; fallback only | EUR 213 | EUR 245 | EUR 309.93 | Hold quality if reopened | EUR 238 | EUR 275 | EUR 345.44 | Cancellation premium | EUR 267 | Booked; Balanced if reopened |
| 2026-08-16 | Sunday | 2 nights | EUR 205 | EUR 272.60 | Booked; fallback only | EUR 205 | EUR 230 | EUR 302.18 | Hold quality if reopened | EUR 230 | EUR 260 | EUR 337.68 | Cancellation premium | EUR 260 | Booked; Balanced if reopened |
| 2026-08-17 | Monday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-18 | Tuesday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-19 | Wednesday | 4 nights | EUR 190 | EUR 239.85 | Scarce Aug but accessible | EUR 184 | EUR 215 | EUR 269.43 | Scarce Aug demand | EUR 209 | EUR 245 | EUR 304.93 | Scarcity and prime comps | EUR 238 | Balanced |
| 2026-08-20 | Thursday | 4 nights | EUR 205 | EUR 257.60 | Scarce Aug but accessible | EUR 199 | EUR 230 | EUR 287.18 | Scarce Aug demand | EUR 223 | EUR 260 | EUR 322.68 | Scarcity and prime comps | EUR 252 | Aggressive |
| 2026-08-21 | Friday | 3 nights | EUR 220 | EUR 280.35 | Scarce Aug but accessible | EUR 213 | EUR 245 | EUR 309.93 | Scarce Aug demand | EUR 238 | EUR 275 | EUR 345.44 | Scarcity and prime comps | EUR 267 | Aggressive |
| 2026-08-22 | Saturday | 3 nights | EUR 220 | EUR 280.35 | Scarce Aug but accessible | EUR 213 | EUR 245 | EUR 309.93 | Scarce Aug demand | EUR 238 | EUR 275 | EUR 345.44 | Scarcity and prime comps | EUR 267 | Aggressive |
| 2026-08-23 | Sunday | 2 nights | EUR 205 | EUR 272.60 | Booked; fallback only | EUR 205 | EUR 230 | EUR 302.18 | Hold quality if reopened | EUR 230 | EUR 260 | EUR 337.68 | Cancellation premium | EUR 260 | Booked; Balanced if reopened |
| 2026-08-24 | Monday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-25 | Tuesday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-26 | Wednesday | 2 nights | EUR 190 | EUR 254.85 | Booked; fallback only | EUR 190 | EUR 215 | EUR 284.43 | Hold quality if reopened | EUR 215 | EUR 245 | EUR 319.93 | Cancellation premium | EUR 245 | Booked; Balanced if reopened |
| 2026-08-27 | Thursday | 2 nights | EUR 205 | EUR 272.60 | Booked; fallback only | EUR 205 | EUR 230 | EUR 302.18 | Hold quality if reopened | EUR 230 | EUR 260 | EUR 337.68 | Cancellation premium | EUR 260 | Booked; Balanced if reopened |
| 2026-08-28 | Friday | 3 nights | EUR 220 | EUR 280.35 | Booked; fallback only | EUR 213 | EUR 245 | EUR 309.93 | Hold quality if reopened | EUR 238 | EUR 275 | EUR 345.44 | Cancellation premium | EUR 267 | Booked; Balanced if reopened |
| 2026-08-29 | Saturday | 3 nights | EUR 220 | EUR 280.35 | Booked; fallback only | EUR 213 | EUR 245 | EUR 309.93 | Hold quality if reopened | EUR 238 | EUR 275 | EUR 345.44 | Cancellation premium | EUR 267 | Booked; Balanced if reopened |
| 2026-08-30 | Sunday | 2 nights | EUR 205 | EUR 272.60 | Booked; fallback only | EUR 205 | EUR 230 | EUR 302.18 | Hold quality if reopened | EUR 230 | EUR 260 | EUR 337.68 | Cancellation premium | EUR 260 | Booked; Balanced if reopened |
| 2026-08-31 | Monday | 2 nights | EUR 190 | EUR 254.85 | Scarce Aug but accessible | EUR 190 | EUR 215 | EUR 284.43 | Scarce Aug demand | EUR 215 | EUR 245 | EUR 319.93 | Scarcity and prime comps | EUR 245 | Balanced |
| 2026-09-01 | Tuesday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-02 | Wednesday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-03 | Thursday | 2 nights | EUR 175 | EUR 237.09 | Peak-tail visibility | EUR 175 | EUR 195 | EUR 260.76 | ADR and occupancy mix | EUR 195 | EUR 220 | EUR 290.35 | Peak-tail upside | EUR 220 | Balanced |
| 2026-09-04 | Friday | 3 nights | EUR 190 | EUR 244.85 | Peak-tail visibility | EUR 184 | EUR 210 | EUR 268.51 | ADR and occupancy mix | EUR 204 | EUR 235 | EUR 298.10 | Peak-tail upside | EUR 228 | Balanced |
| 2026-09-05 | Saturday | 3 nights | EUR 190 | EUR 244.85 | Peak-tail visibility | EUR 184 | EUR 210 | EUR 268.51 | ADR and occupancy mix | EUR 204 | EUR 235 | EUR 298.10 | Peak-tail upside | EUR 228 | Balanced |
| 2026-09-06 | Sunday | 2 nights | EUR 175 | EUR 237.09 | Peak-tail visibility | EUR 175 | EUR 195 | EUR 260.76 | ADR and occupancy mix | EUR 195 | EUR 220 | EUR 290.35 | Peak-tail upside | EUR 220 | Balanced |
| 2026-09-07 | Monday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-08 | Tuesday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-09 | Wednesday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-10 | Thursday | 2 nights | EUR 175 | EUR 237.09 | Peak-tail visibility | EUR 175 | EUR 195 | EUR 260.76 | ADR and occupancy mix | EUR 195 | EUR 220 | EUR 290.35 | Peak-tail upside | EUR 220 | Balanced |
| 2026-09-11 | Friday | 3 nights | EUR 190 | EUR 244.85 | Peak-tail visibility | EUR 184 | EUR 210 | EUR 268.51 | ADR and occupancy mix | EUR 204 | EUR 235 | EUR 298.10 | Peak-tail upside | EUR 228 | Balanced |
| 2026-09-12 | Saturday | 3 nights | EUR 190 | EUR 244.85 | Peak-tail visibility | EUR 184 | EUR 210 | EUR 268.51 | ADR and occupancy mix | EUR 204 | EUR 235 | EUR 298.10 | Peak-tail upside | EUR 228 | Balanced |
| 2026-09-13 | Sunday | 2 nights | EUR 175 | EUR 237.09 | Peak-tail visibility | EUR 175 | EUR 195 | EUR 260.76 | ADR and occupancy mix | EUR 195 | EUR 220 | EUR 290.35 | Peak-tail upside | EUR 220 | Balanced |
| 2026-09-14 | Monday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-15 | Tuesday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-16 | Wednesday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-17 | Thursday | 2 nights | EUR 175 | EUR 237.09 | Peak-tail visibility | EUR 175 | EUR 195 | EUR 260.76 | ADR and occupancy mix | EUR 195 | EUR 220 | EUR 290.35 | Peak-tail upside | EUR 220 | Balanced |
| 2026-09-18 | Friday | 3 nights | EUR 190 | EUR 244.85 | Peak-tail visibility | EUR 184 | EUR 210 | EUR 268.51 | ADR and occupancy mix | EUR 204 | EUR 235 | EUR 298.10 | Peak-tail upside | EUR 228 | Balanced |
| 2026-09-19 | Saturday | 3 nights | EUR 190 | EUR 244.85 | Peak-tail visibility | EUR 184 | EUR 210 | EUR 268.51 | ADR and occupancy mix | EUR 204 | EUR 235 | EUR 298.10 | Peak-tail upside | EUR 228 | Balanced |
| 2026-09-20 | Sunday | 2 nights | EUR 175 | EUR 237.09 | Peak-tail visibility | EUR 175 | EUR 195 | EUR 260.76 | ADR and occupancy mix | EUR 195 | EUR 220 | EUR 290.35 | Peak-tail upside | EUR 220 | Balanced |
| 2026-09-21 | Monday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-22 | Tuesday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-23 | Wednesday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-24 | Thursday | 2 nights | EUR 175 | EUR 237.09 | Peak-tail visibility | EUR 175 | EUR 195 | EUR 260.76 | ADR and occupancy mix | EUR 195 | EUR 220 | EUR 290.35 | Peak-tail upside | EUR 220 | Balanced |
| 2026-09-25 | Friday | 3 nights | EUR 190 | EUR 244.85 | Peak-tail visibility | EUR 184 | EUR 210 | EUR 268.51 | ADR and occupancy mix | EUR 204 | EUR 235 | EUR 298.10 | Peak-tail upside | EUR 228 | Balanced |
| 2026-09-26 | Saturday | 3 nights | EUR 190 | EUR 244.85 | Peak-tail visibility | EUR 184 | EUR 210 | EUR 268.51 | ADR and occupancy mix | EUR 204 | EUR 235 | EUR 298.10 | Peak-tail upside | EUR 228 | Balanced |
| 2026-09-27 | Sunday | 2 nights | EUR 175 | EUR 237.09 | Peak-tail visibility | EUR 175 | EUR 195 | EUR 260.76 | ADR and occupancy mix | EUR 195 | EUR 220 | EUR 290.35 | Peak-tail upside | EUR 220 | Balanced |
| 2026-09-28 | Monday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-29 | Tuesday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-09-30 | Wednesday | 2 nights | EUR 160 | EUR 219.34 | Peak-tail visibility | EUR 160 | EUR 180 | EUR 243.01 | ADR and occupancy mix | EUR 180 | EUR 205 | EUR 272.60 | Peak-tail upside | EUR 205 | Balanced |
| 2026-10-01 | Thursday | 2 nights | EUR 145 | EUR 201.59 | Shoulder occupancy | EUR 145 | EUR 165 | EUR 225.26 | Shoulder mix | EUR 165 | EUR 200 | EUR 266.68 | Advance upside | EUR 200 | Balanced |
| 2026-10-02 | Friday | 2 nights | EUR 160 | EUR 219.34 | Shoulder occupancy | EUR 160 | EUR 180 | EUR 243.01 | Shoulder mix | EUR 180 | EUR 215 | EUR 284.43 | Advance upside | EUR 215 | Balanced |
| 2026-10-03 | Saturday | 2 nights | EUR 160 | EUR 219.34 | Shoulder occupancy | EUR 160 | EUR 180 | EUR 243.01 | Shoulder mix | EUR 180 | EUR 215 | EUR 284.43 | Advance upside | EUR 215 | Balanced |
| 2026-10-04 | Sunday | 2 nights | EUR 145 | EUR 201.59 | Shoulder occupancy | EUR 145 | EUR 165 | EUR 225.26 | Shoulder mix | EUR 165 | EUR 200 | EUR 266.68 | Advance upside | EUR 200 | Balanced |
| 2026-10-05 | Monday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-06 | Tuesday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-07 | Wednesday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-08 | Thursday | 2 nights | EUR 145 | EUR 201.59 | Shoulder occupancy | EUR 145 | EUR 165 | EUR 225.26 | Shoulder mix | EUR 165 | EUR 200 | EUR 266.68 | Advance upside | EUR 200 | Balanced |
| 2026-10-09 | Friday | 2 nights | EUR 160 | EUR 219.34 | Shoulder occupancy | EUR 160 | EUR 180 | EUR 243.01 | Shoulder mix | EUR 180 | EUR 215 | EUR 284.43 | Advance upside | EUR 215 | Balanced |
| 2026-10-10 | Saturday | 2 nights | EUR 160 | EUR 219.34 | Shoulder occupancy | EUR 160 | EUR 180 | EUR 243.01 | Shoulder mix | EUR 180 | EUR 215 | EUR 284.43 | Advance upside | EUR 215 | Balanced |
| 2026-10-11 | Sunday | 2 nights | EUR 145 | EUR 201.59 | Shoulder occupancy | EUR 145 | EUR 165 | EUR 225.26 | Shoulder mix | EUR 165 | EUR 200 | EUR 266.68 | Advance upside | EUR 200 | Balanced |
| 2026-10-12 | Monday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-13 | Tuesday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-14 | Wednesday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-15 | Thursday | 2 nights | EUR 145 | EUR 201.59 | Shoulder occupancy | EUR 145 | EUR 165 | EUR 225.26 | Shoulder mix | EUR 165 | EUR 200 | EUR 266.68 | Advance upside | EUR 200 | Balanced |
| 2026-10-16 | Friday | 2 nights | EUR 160 | EUR 219.34 | Shoulder occupancy | EUR 160 | EUR 180 | EUR 243.01 | Shoulder mix | EUR 180 | EUR 215 | EUR 284.43 | Advance upside | EUR 215 | Balanced |
| 2026-10-17 | Saturday | 2 nights | EUR 160 | EUR 219.34 | Shoulder occupancy | EUR 160 | EUR 180 | EUR 243.01 | Shoulder mix | EUR 180 | EUR 215 | EUR 284.43 | Advance upside | EUR 215 | Balanced |
| 2026-10-18 | Sunday | 2 nights | EUR 145 | EUR 201.59 | Shoulder occupancy | EUR 145 | EUR 165 | EUR 225.26 | Shoulder mix | EUR 165 | EUR 200 | EUR 266.68 | Advance upside | EUR 200 | Balanced |
| 2026-10-19 | Monday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-20 | Tuesday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-21 | Wednesday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-22 | Thursday | 2 nights | EUR 145 | EUR 201.59 | Shoulder occupancy | EUR 145 | EUR 165 | EUR 225.26 | Shoulder mix | EUR 165 | EUR 200 | EUR 266.68 | Advance upside | EUR 200 | Balanced |
| 2026-10-23 | Friday | 2 nights | EUR 160 | EUR 219.34 | Shoulder occupancy | EUR 160 | EUR 180 | EUR 243.01 | Shoulder mix | EUR 180 | EUR 215 | EUR 284.43 | Advance upside | EUR 215 | Balanced |
| 2026-10-24 | Saturday | 2 nights | EUR 160 | EUR 219.34 | Shoulder occupancy | EUR 160 | EUR 180 | EUR 243.01 | Shoulder mix | EUR 180 | EUR 215 | EUR 284.43 | Advance upside | EUR 215 | Balanced |
| 2026-10-25 | Sunday | 2 nights | EUR 145 | EUR 201.59 | Shoulder occupancy | EUR 145 | EUR 165 | EUR 225.26 | Shoulder mix | EUR 165 | EUR 200 | EUR 266.68 | Advance upside | EUR 200 | Balanced |
| 2026-10-26 | Monday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-27 | Tuesday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-28 | Wednesday | 2 nights | EUR 130 | EUR 183.84 | Shoulder occupancy | EUR 130 | EUR 150 | EUR 207.51 | Shoulder mix | EUR 150 | EUR 185 | EUR 248.93 | Advance upside | EUR 185 | Conservative |
| 2026-10-29 | Thursday | 2 nights | EUR 145 | EUR 201.59 | Shoulder occupancy | EUR 145 | EUR 165 | EUR 225.26 | Shoulder mix | EUR 165 | EUR 200 | EUR 266.68 | Advance upside | EUR 200 | Balanced |

## Bottom Summary - Proposed Strategy

The listing is already converting in peak season, so the strategy should avoid training the market to expect heavy discounts. Use Balanced as the daily default, Conservative only for July open dates that are close to arrival or awkward to fill, and Aggressive for scarce August inventory and weekend starts. Keep the base net rate high enough that even after modest 3-night or last-minute discounts the guest-facing gross price remains consistent with premium Gzira/Sliema alternatives.

The strongest operational move is to watch July 11-18 and July 21-28 every 48-72 hours. If July 11-13 has not converted by July 5, switch those specific starts to Conservative while keeping Friday/Saturday prices Balanced. If August 19-22 remains open after August 5, first reduce minimum stay flexibility before cutting price; a four-night or two-plus-two split at strong ADR is preferable to a broad August markdown.

## Bottom Summary - Competitor Trends

Direct premium Gzira demand supports a high ceiling: the closest seafront Gzira 1BR comp is pricing around EUR 233.50 gross near-term and EUR 292.23 gross in August while still only 39%-45% occupied for July/August. That means the internal listing can hold premium August net rates without needing to match lower-quality Gzira stock.

Lower-quality Gzira groups show bifurcation. Some are deeply booked at lower rates, while others still have substantial August availability. This argues against using the lower comp average as the target; those listings are useful for search-visibility guardrails, not as the revenue ceiling for a very high finish BBQ terrace listing.

The unusual price spikes and unreliable observations are concentrated in boundary groups: several Sliema and Gzira 2BR reports contain EUR 0.x prices that are not economically plausible, and at least one 2BR Gzira competitor is effectively 100% occupied/closed with no usable pricing. These were excluded from rate setting. Valid premium boundary observations around EUR 270-315 gross justify aggressive August/weekend testing, but only as a ceiling check.

Event context is only partial. VisitMalta's official events page was consulted, but the text extraction did not expose clean dated event listings. Fixed holiday-risk dates inside the calendar, especially August 15, September 8, and September 21, should be watched manually for demand spikes and not automatically discounted.

## assistantContext

### Checklist

- (Done) Confirmed default internalGroupId: `1bedroom-very-high-finish-very-high-amenities-side-sea-view-gzira`.
- (Done) Generated AirGPT conversation ID: `019f1422-8f22-7c64-bd84-29cb1a8fab0b`.
- (Done) Retrieved AirGPT current UTC date/time: 2026-06-29T16:07:23.595425600.
- (Done) Retrieved competitor metadata.
- (Done) Retrieved available report days for direct, local, Sliema, and premium-boundary groups.
- (Done) Retrieved latest internal report: 2026-06-28.
- (Done) Retrieved multiple internal historical reports: 2026-06-21 and 2026-06-15.
- (Done) Retrieved latest direct comp report: Gzira 1BR high finish/high amenities seafront view, 2026-06-29.
- (Done) Retrieved latest and historical lower-bound Gzira reports: close-to-seafront medium/no-view and terrace groups, 2026-06-15, 2026-06-21, 2026-06-28.
- (Done) Retrieved Sliema and 2BR premium boundary reports: 2026-06-28.
- (Done/Partial) Consulted event context using AirGPT `getWebsite` for https://www.visitmalta.com/en/events-in-malta-and-gozo/; page extraction did not expose detailed event dates.
- (Done) Applied minimum net-rate and gross-price formula.
- (Done) Generated next-4-month daily pricing calendar through 2026-10-29.

### Accrued Information

- Internal listing is open for bookings, launched 2025-10-27, minimum net rate EUR 90, cleaning fee EUR 60, max occupancy 4, terrace true, barbecue true, pool false.
- Current internal discounts before this strategy: 15% last-minute within 5 days; 8% for 3+ nights; 10% for 5+; 14% for 10+; 18% for 14+; 20% for 28+.
- Latest internal report showed no immediate availability, July average gross EUR 224.17, August average gross EUR 190.25, July occupancy 48%, August occupancy 84%.
- Internal historical report on 2026-06-21 had July gross EUR 246.29 and August gross EUR 236.25; by 2026-06-28 July/August gross had been reduced while August occupancy stayed high.
- Direct Gzira seafront 1BR comp reported EUR 233.50 near-term gross, EUR 239.67 July gross, and EUR 292.23 August gross.
- Lower local comps mostly support a lower boundary from about EUR 129-216 gross, depending on amenity package and month.
- Valid premium boundary comps support higher ceilings around EUR 270-315 gross, but several boundary reports include bad EUR 0.x artifacts.

### Assumptions

- Competitor report prices are gross/guest-facing and include the Airbnb guest service fee plus cleaning effects.
- Proposed prices are net host-facing nightly rates before guest service fee and before amortized cleaning fee.
- Calendar dates beyond August were not exposed in the latest internal monthly availability section; September and October are assumed open unless the live calendar has changed.
- Event-specific pricing is not heavily adjusted because official event-date extraction was incomplete. Fixed Malta holiday dates are treated as manual monitoring points.
- The listing should preserve a premium brand perception because current occupancy and listing quality do not support broad deep discounting.

### Calculation Summary

- Gross daily price is calculated as `(net * 1.1834) + (60 / minStay)`.
- Discount-adjusted net is calculated by sequential multiplication of duration and last-minute discounts and then floored at EUR 90.
- Base net recommendations use season, weekday/weekend, internal availability, direct-comp ceiling, lower-bound comp floors, and listing age/conversion signals.
- July Balanced rates were set mostly at EUR 180-210 net to remain below premium Gzira/Sliema ceilings while staying materially above lower-quality Gzira stock.
- August Balanced rates were set mostly at EUR 215-245 net because August inventory is scarce and premium comps support stronger gross pricing.
- September tapers back toward July rates; October moves to shoulder-season occupancy pricing.

### Confidence Level

Medium.

Confidence is medium because there are many internal and competitor snapshots, clear internal occupancy signals, and a usable direct Gzira premium comp. Confidence is not high because the direct premium Gzira comp has only one historical report, several boundary groups contain unreliable EUR 0.x artifacts, and external event-date extraction was incomplete.
