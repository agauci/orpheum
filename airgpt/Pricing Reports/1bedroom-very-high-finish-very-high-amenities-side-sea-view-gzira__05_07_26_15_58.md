# Pricing Strategy Report: 1bedroom-very-high-finish-very-high-amenities-side-sea-view-gzira

Report timestamp: 2026-07-05T15:58:10.602860400 UTC

Revision note: updated on 2026-07-05 after the host reported that remaining July inventory has had exposure without selling. The July recommendation now treats close open gaps as stale, price-sensitive inventory rather than fresh peak-season inventory.

## Executive Recommendation

Use a July conversion-rescue model for remaining open July gaps, Balanced for October, Aggressive for the scarce open peak-season gaps from 2026-08-19 through 2026-09-04, and Conservative only for immediate cancellation recovery or early-November conversion support. The internal listing is open for bookings, has a minimum allowed net rate of EUR 90, a cleaning fee of EUR 60, and is materially ahead of most close comparables on August and September occupancy.

The strongest signal is internal scarcity: the latest internal report shows July at 43% occupied, August at 84% occupied, and September at 87% occupied. August accelerated from 58% occupied on 2026-06-15 to 84% occupied by 2026-06-25, so the remaining peak-season nights should be protected rather than discounted aggressively.

The July revision is driven by a different signal: the remaining July dates have been exposed without selling, while competitor momentum shows lower-quality/lower-priced stock moving and premium or mid-premium July gaps staying open or cutting prices. That makes the close July span stale inventory, so conversion is now more important than defending the earlier Balanced ADR.

## Pricing Models

- Conservative: prioritize occupancy, even at the cost of suppressed rates.
- Balanced: maximize ADR while seeking an optimal balance between occupancy and daily rates.
- Aggressive: seek the highest daily rates, accepting the risk of suppressed occupancy.

## Core Rules

- Primary recommendation uses net nightly prices excluding Airbnb service fee and excluding amortized cleaning fee.
- Minimum allowed net rate: EUR 90. Neither base nor discount-adjusted prices go below this.
- Gross daily price formula: `grossDailyPrice = (netDailyPrice * 1.1834) + (60 / suggestedMinimumStayNights)`.
- Discount stacking convention: `discountedNet = max(90, netDailyPrice * (1 - durationDiscount) * (1 - lastMinuteDiscount))`, rounded to the nearest EUR 5 for operational use.
- Competitor prices are treated as gross Airbnb-facing prices, so proposed net rates are compared using the gross conversion above.

## Discount Strategy

| Discount type | Recommended rule | Rationale |
|---|---:|---|
| 3-night stay | 0% in July-September; 3% in October-November | Keep peak-season brand perception strong; use small shoulder-season incentive only where useful. |
| 5-night stay | 4% in July-September; 6% in October-November | Rewards useful gaps without diluting ADR too heavily. |
| 10-night stay | 8% in July-September; 10% in October-November | Helps longer stays in softer periods while preserving peak value. |
| 14-night stay | 10% in July-September; 12% in October-November | Suitable for medium stays outside the scarcest peak dates. |
| 28-night stay | 14% in July-September; 16% in October-November | Monthly stays should be accepted selectively, not as a default peak-season strategy. |
| Last-minute | 10% for remaining open July arrivals within 7 days; 8% for other July-September cancellation inventory; 12% within 7 days in October-November | July gaps have stale exposure, so the close-open span needs a stronger conversion nudge while still preserving the EUR 90 floor. |
| Stale near-term override | Reduce base July rescue prices by 10-20% versus the original Balanced model before applying last-minute discounts | Competitor momentum shows cheaper stock moving while premium/mid-premium July gaps remain soft. |

## Immediate Horizon: Next 7 Days

| Date | Status | Conservative net | Balanced net | Aggressive net | Recommended model |
|---|---|---:|---:|---:|---|
| 2026-07-05 | Booked/internal calendar closed | EUR 150 | EUR 165 | EUR 190 | Balanced if reopened |
| 2026-07-06 | Booked/internal calendar closed | EUR 140 | EUR 155 | EUR 180 | Balanced if reopened |
| 2026-07-07 | Booked/internal calendar closed | EUR 135 | EUR 150 | EUR 175 | Balanced if reopened |
| 2026-07-08 | Booked/internal calendar closed | EUR 140 | EUR 155 | EUR 180 | Balanced if reopened |
| 2026-07-09 | Booked/internal calendar closed | EUR 150 | EUR 165 | EUR 190 | Balanced if reopened |
| 2026-07-10 | Booked/internal calendar closed | EUR 160 | EUR 175 | EUR 200 | Balanced if reopened |
| 2026-07-11 | Available | EUR 135 | EUR 150 | EUR 170 | Balanced rescue |

Immediate action: keep the booked 2026-07-05 through 2026-07-10 dates unchanged unless a cancellation opens. For 2026-07-11, price in the rescue band now: EUR 150 net as the working target, with a 10% last-minute discount if it is still open inside the 7-day booking window. If no inquiry or booking appears within 48 hours, move to the Conservative rescue rate.

## Short-Term Horizon: Day 7 Through Day 14

| Date | Status | Conservative net | Balanced net | Aggressive net | Recommended model |
|---|---|---:|---:|---:|---|
| 2026-07-12 | Available | EUR 130 | EUR 145 | EUR 165 | Conservative rescue |
| 2026-07-13 | Available | EUR 120 | EUR 130 | EUR 150 | Conservative rescue |
| 2026-07-14 | Available | EUR 115 | EUR 125 | EUR 145 | Conservative rescue |
| 2026-07-15 | Available | EUR 120 | EUR 130 | EUR 150 | Conservative rescue |
| 2026-07-16 | Available | EUR 125 | EUR 140 | EUR 160 | Conservative rescue |
| 2026-07-17 | Available | EUR 140 | EUR 155 | EUR 180 | Balanced rescue |
| 2026-07-18 | Available | EUR 145 | EUR 160 | EUR 185 | Balanced rescue |

Short-term action: treat the 2026-07-12 through 2026-07-18 span as stale, close-in inventory. The target should be clearly below medium Gzira terrace competitors that stayed open or cut prices, not merely below the direct seafront competitor. Use the Conservative rescue model for weekdays and the Balanced rescue model for Friday/Saturday.

## Medium-Term Suggestions

| Horizon date | Calendar status | Conservative net | Balanced net | Aggressive net | Suggested action |
|---|---|---:|---:|---:|---|
| 2026-08-05 | Booked/internal calendar closed | EUR 185 | EUR 205 | EUR 235 | No action unless cancellation; if reopened, use balanced/aggressive because internal occupancy is already strong. |
| 2026-09-05 | Booked/internal calendar closed | EUR 195 | EUR 215 | EUR 245 | No action unless cancellation; if reopened, use balanced/aggressive because internal occupancy is already strong. |

Medium-term action: August and early September should not be treated as soft inventory. The listing is already highly occupied, and direct seafront Gzira pricing supports materially higher gross prices than the current internal average on many remaining peak dates.

## Current Performance Analysis

The listing has been live since 2025-10-27 and is accepting bookings. By 2026-07-04, the calendar shows no immediate-term availability, moderate remaining July supply, and very limited August/September supply. This is a strong conversion signal for an approximately eight-month-old premium listing.

Internal pricing is no longer conservative enough for the remaining stale July gaps. The latest internal short-term average is EUR 178.20 gross for 2026-07-11 onward, while same-period competitor evidence shows cheaper/lower-quality stock gaining occupancy and mid-premium July gaps cutting or remaining open. For August and September, the story is different: internal scarcity is strong, so do not extrapolate the July rescue cut into those months.

## Competitor Trend Summary

- Direct Gzira 1BR seafront competitor: latest gross averages are EUR 223.38 immediate, EUR 233.50 short-term, EUR 241.23 for July, EUR 290.60 for August, and EUR 265.63 for September. Its July 14-18 availability remained open around EUR 233.50 gross, so it is a stale premium ceiling rather than a target to mirror.
- Internal listing: July remains 43% occupied, while August and September are already 84% and 87% occupied. August tightened rapidly between 2026-06-15 and 2026-06-25, which supports a higher remaining-inventory rate.
- Lower-quality close Gzira 1BR groups: one comparable moved from 87% to 100% July occupancy and another from 45% to 89%, showing that demand exists when value is obvious.
- Medium Gzira terrace group: valid competitors range widely, but several exact July gap dates stayed open or saw cuts. Nevermind dropped comparable July spans from about EUR 204.50 to EUR 153.50 gross, and Juliet moved from about EUR 179-EUR 193 to EUR 147-EUR 172 gross. Mili's EUR 99.50 pricing with 0% July occupancy remains a weak conversion signal, so do not chase it fully.
- Sliema 1BR high-finish no-view: EUR 158 immediate, EUR 193 short-term, EUR 226 August, with low occupancy. This warns against pushing too far above EUR 220 gross for ordinary non-view 1BR demand.
- Sliema 2BR seafront groups: valid spans around EUR 300-EUR 338 gross show premium market ceiling, but several reports contain impossible EUR 0.35-EUR 0.56 values. Those values are treated as scraping/report artifacts and excluded.
- Suspicious information: impossible sub-EUR 1 prices, the internal 2026-06-15 EUR 0.28 July span, and groups with sustained 100% occupancy are flagged as unreliable or unavailable/offline signals rather than market price signals.

## Four-Month Daily Pricing Calendar

| Date | Day of week | Suggested minimum stay | Conservative net | Conservative gross | Conservative logic | Conservative discounted net | Balanced net | Balanced gross | Balanced logic | Balanced discounted net | Aggressive net | Aggressive gross | Aggressive logic | Aggressive discounted net | AirGPT recommendation |
|---|---|---:|---:|---:|---|---:|---:|---:|---|---:|---:|---:|---|---:|---|
| 2026-07-05 | Sunday | 2 | EUR 150 | EUR 207.51 | Booked; if reopened use last-minute recovery. | EUR 140 | EUR 165 | EUR 225.26 | Booked; if reopened stay under direct seafront comp. | EUR 150 | EUR 190 | EUR 254.85 | Booked; if reopened capture peak cancellation demand. | EUR 175 | Balanced if reopened |
| 2026-07-06 | Monday | 2 | EUR 140 | EUR 195.68 | Booked; if reopened use last-minute recovery. | EUR 130 | EUR 155 | EUR 213.43 | Booked; if reopened stay under direct seafront comp. | EUR 145 | EUR 180 | EUR 243.01 | Booked; if reopened capture peak cancellation demand. | EUR 165 | Balanced if reopened |
| 2026-07-07 | Tuesday | 2 | EUR 135 | EUR 189.76 | Booked; if reopened use last-minute recovery. | EUR 125 | EUR 150 | EUR 207.51 | Booked; if reopened stay under direct seafront comp. | EUR 140 | EUR 175 | EUR 237.09 | Booked; if reopened capture peak cancellation demand. | EUR 160 | Balanced if reopened |
| 2026-07-08 | Wednesday | 2 | EUR 140 | EUR 195.68 | Booked; if reopened use last-minute recovery. | EUR 130 | EUR 155 | EUR 213.43 | Booked; if reopened stay under direct seafront comp. | EUR 145 | EUR 180 | EUR 243.01 | Booked; if reopened capture peak cancellation demand. | EUR 165 | Balanced if reopened |
| 2026-07-09 | Thursday | 2 | EUR 150 | EUR 207.51 | Booked; if reopened use last-minute recovery. | EUR 140 | EUR 165 | EUR 225.26 | Booked; if reopened stay under direct seafront comp. | EUR 150 | EUR 190 | EUR 254.85 | Booked; if reopened capture peak cancellation demand. | EUR 175 | Balanced if reopened |
| 2026-07-10 | Friday | 2 | EUR 160 | EUR 219.34 | Booked; if reopened use last-minute recovery. | EUR 145 | EUR 175 | EUR 237.09 | Booked; if reopened stay under direct seafront comp. | EUR 160 | EUR 200 | EUR 266.68 | Booked; if reopened capture peak cancellation demand. | EUR 185 | Balanced if reopened |
| 2026-07-11 | Saturday | 2 | EUR 135 | EUR 189.76 | Stale July gap; weekend rescue floor. | EUR 120 | EUR 150 | EUR 207.51 | Stale July gap; undercut mid comps after discount. | EUR 135 | EUR 170 | EUR 231.18 | Weekend ceiling only if pickup starts. | EUR 155 | Balanced rescue |
| 2026-07-12 | Sunday | 2 | EUR 130 | EUR 183.84 | Stale July gap; Sunday rescue floor. | EUR 115 | EUR 145 | EUR 201.59 | Near-term stale demand, clear value. | EUR 130 | EUR 165 | EUR 225.26 | Soft premium ceiling. | EUR 150 | Conservative rescue |
| 2026-07-13 | Monday | 2 | EUR 120 | EUR 172.01 | Stale weekday gap; prioritize conversion. | EUR 120 | EUR 130 | EUR 183.84 | Beat mid comps; protect EUR 90 floor. | EUR 130 | EUR 150 | EUR 207.51 | Only if inquiries improve. | EUR 150 | Conservative rescue |
| 2026-07-14 | Tuesday | 2 | EUR 115 | EUR 166.09 | Soft Tuesday; conversion floor. | EUR 115 | EUR 125 | EUR 177.93 | Price below stale mid comps. | EUR 125 | EUR 145 | EUR 201.59 | Small premium for quality. | EUR 145 | Conservative rescue |
| 2026-07-15 | Wednesday | 2 | EUR 120 | EUR 172.01 | Stale Wednesday gap; prioritize conversion. | EUR 120 | EUR 130 | EUR 183.84 | Beat mid comps; protect brand floor. | EUR 130 | EUR 150 | EUR 207.51 | Only if pickup improves. | EUR 150 | Conservative rescue |
| 2026-07-16 | Thursday | 2 | EUR 125 | EUR 177.93 | Stale Thursday; conversion-led rate. | EUR 125 | EUR 140 | EUR 195.68 | Below mid comps after weak momentum. | EUR 140 | EUR 160 | EUR 219.34 | Premium test but monitor. | EUR 160 | Conservative rescue |
| 2026-07-17 | Friday | 2 | EUR 140 | EUR 195.68 | Friday rescue; keep some weekend premium. | EUR 140 | EUR 155 | EUR 213.43 | Weekend value below premium comp. | EUR 155 | EUR 180 | EUR 243.01 | Weekend ceiling if inquiries return. | EUR 180 | Balanced rescue |
| 2026-07-18 | Saturday | 2 | EUR 145 | EUR 201.59 | Saturday rescue; protect weekend value. | EUR 145 | EUR 160 | EUR 219.34 | Weekend value under premium comp. | EUR 160 | EUR 185 | EUR 248.93 | Weekend ceiling only with pickup. | EUR 185 | Balanced rescue |
| 2026-07-19 | Sunday | 2 | EUR 160 | EUR 219.34 | Late-July open gap, protect occupancy. | EUR 160 | EUR 180 | EUR 243.01 | Late-July comp-aligned rate for premium terrace stock. | EUR 180 | EUR 210 | EUR 278.51 | Late-July yield push against strong Gzira comps. | EUR 210 | Balanced if reopened |
| 2026-07-20 | Monday | 2 | EUR 150 | EUR 207.51 | Late-July open gap, protect occupancy. | EUR 150 | EUR 170 | EUR 231.18 | Late-July comp-aligned rate for premium terrace stock. | EUR 170 | EUR 200 | EUR 266.68 | Late-July yield push against strong Gzira comps. | EUR 200 | Balanced if reopened |
| 2026-07-21 | Tuesday | 2 | EUR 125 | EUR 177.93 | Stale late-July weekday; conversion floor. | EUR 125 | EUR 140 | EUR 195.68 | Stay below stale mid comps. | EUR 140 | EUR 160 | EUR 219.34 | Ceiling only if early gap sells. | EUR 160 | Conservative rescue |
| 2026-07-22 | Wednesday | 2 | EUR 130 | EUR 183.84 | Stale late-July weekday. | EUR 130 | EUR 145 | EUR 201.59 | Mid-market value signal. | EUR 145 | EUR 165 | EUR 225.26 | Quality ceiling if pickup improves. | EUR 165 | Conservative rescue |
| 2026-07-23 | Thursday | 2 | EUR 135 | EUR 189.76 | Stale late-July shoulder-to-weekend. | EUR 135 | EUR 150 | EUR 207.51 | Clear value below premium comps. | EUR 150 | EUR 175 | EUR 237.09 | Quality ceiling if pickup improves. | EUR 175 | Balanced rescue |
| 2026-07-24 | Friday | 2 | EUR 150 | EUR 207.51 | Friday, still rescue band. | EUR 150 | EUR 170 | EUR 231.18 | Weekend value; avoid overcutting. | EUR 170 | EUR 195 | EUR 260.76 | Weekend premium test. | EUR 195 | Balanced rescue |
| 2026-07-25 | Saturday | 2 | EUR 155 | EUR 213.43 | Saturday, protect weekend value. | EUR 155 | EUR 175 | EUR 237.09 | Weekend balanced rescue. | EUR 175 | EUR 200 | EUR 266.68 | Premium ceiling. | EUR 200 | Balanced rescue |
| 2026-07-26 | Sunday | 2 | EUR 140 | EUR 195.68 | Sunday rescue. | EUR 140 | EUR 155 | EUR 213.43 | Balanced value for post-weekend stay. | EUR 155 | EUR 180 | EUR 243.01 | Ceiling if pickup returns. | EUR 180 | Balanced rescue |
| 2026-07-27 | Monday | 2 | EUR 130 | EUR 183.84 | Late-July weekday rescue. | EUR 130 | EUR 145 | EUR 201.59 | Below mid/premium stale comps. | EUR 145 | EUR 165 | EUR 225.26 | Ceiling only if gap tightens. | EUR 165 | Conservative rescue |
| 2026-07-28 | Tuesday | 2 | EUR 125 | EUR 177.93 | Soft Tuesday rescue. | EUR 125 | EUR 140 | EUR 195.68 | Stay competitive if still open. | EUR 140 | EUR 160 | EUR 219.34 | Ceiling only if pickup returns. | EUR 160 | Conservative rescue |
| 2026-07-29 | Wednesday | 2 | EUR 150 | EUR 207.51 | Late-July open gap, protect occupancy. | EUR 150 | EUR 170 | EUR 231.18 | Late-July comp-aligned rate for premium terrace stock. | EUR 170 | EUR 200 | EUR 266.68 | Late-July yield push against strong Gzira comps. | EUR 200 | Balanced if reopened |
| 2026-07-30 | Thursday | 2 | EUR 160 | EUR 219.34 | Late-July open gap, protect occupancy. | EUR 160 | EUR 180 | EUR 243.01 | Late-July comp-aligned rate for premium terrace stock. | EUR 180 | EUR 210 | EUR 278.51 | Late-July yield push against strong Gzira comps. | EUR 210 | Balanced if reopened |
| 2026-07-31 | Friday | 2 | EUR 170 | EUR 231.18 | Late-July open gap, protect occupancy. | EUR 170 | EUR 190 | EUR 254.85 | Late-July comp-aligned rate for premium terrace stock. | EUR 190 | EUR 220 | EUR 290.35 | Late-July yield push against strong Gzira comps. | EUR 220 | Balanced if reopened |
| 2026-08-01 | Saturday | 3 | EUR 205 | EUR 262.60 | Booked peak August; cancellation floor. | EUR 205 | EUR 225 | EUR 286.26 | Booked peak August; protect ADR if reopened. | EUR 225 | EUR 255 | EUR 321.77 | Booked peak August; scarcity-led ceiling. | EUR 255 | Balanced if reopened |
| 2026-08-02 | Sunday | 3 | EUR 195 | EUR 250.76 | Booked peak August; cancellation floor. | EUR 195 | EUR 215 | EUR 274.43 | Booked peak August; protect ADR if reopened. | EUR 215 | EUR 245 | EUR 309.93 | Booked peak August; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-08-03 | Monday | 3 | EUR 185 | EUR 238.93 | Booked peak August; cancellation floor. | EUR 185 | EUR 205 | EUR 262.60 | Booked peak August; protect ADR if reopened. | EUR 205 | EUR 235 | EUR 298.10 | Booked peak August; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-08-04 | Tuesday | 3 | EUR 180 | EUR 233.01 | Booked peak August; cancellation floor. | EUR 180 | EUR 200 | EUR 256.68 | Booked peak August; protect ADR if reopened. | EUR 200 | EUR 230 | EUR 292.18 | Booked peak August; scarcity-led ceiling. | EUR 230 | Balanced if reopened |
| 2026-08-05 | Wednesday | 3 | EUR 185 | EUR 238.93 | Booked peak August; cancellation floor. | EUR 185 | EUR 205 | EUR 262.60 | Booked peak August; protect ADR if reopened. | EUR 205 | EUR 235 | EUR 298.10 | Booked peak August; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-08-06 | Thursday | 3 | EUR 195 | EUR 250.76 | Booked peak August; cancellation floor. | EUR 195 | EUR 215 | EUR 274.43 | Booked peak August; protect ADR if reopened. | EUR 215 | EUR 245 | EUR 309.93 | Booked peak August; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-08-07 | Friday | 3 | EUR 205 | EUR 262.60 | Booked peak August; cancellation floor. | EUR 205 | EUR 225 | EUR 286.26 | Booked peak August; protect ADR if reopened. | EUR 225 | EUR 255 | EUR 321.77 | Booked peak August; scarcity-led ceiling. | EUR 255 | Balanced if reopened |
| 2026-08-08 | Saturday | 3 | EUR 205 | EUR 262.60 | Booked peak August; cancellation floor. | EUR 205 | EUR 225 | EUR 286.26 | Booked peak August; protect ADR if reopened. | EUR 225 | EUR 255 | EUR 321.77 | Booked peak August; scarcity-led ceiling. | EUR 255 | Balanced if reopened |
| 2026-08-09 | Sunday | 3 | EUR 195 | EUR 250.76 | Booked peak August; cancellation floor. | EUR 195 | EUR 215 | EUR 274.43 | Booked peak August; protect ADR if reopened. | EUR 215 | EUR 245 | EUR 309.93 | Booked peak August; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-08-10 | Monday | 3 | EUR 185 | EUR 238.93 | Booked peak August; cancellation floor. | EUR 185 | EUR 205 | EUR 262.60 | Booked peak August; protect ADR if reopened. | EUR 205 | EUR 235 | EUR 298.10 | Booked peak August; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-08-11 | Tuesday | 3 | EUR 180 | EUR 233.01 | Booked peak August; cancellation floor. | EUR 180 | EUR 200 | EUR 256.68 | Booked peak August; protect ADR if reopened. | EUR 200 | EUR 230 | EUR 292.18 | Booked peak August; scarcity-led ceiling. | EUR 230 | Balanced if reopened |
| 2026-08-12 | Wednesday | 3 | EUR 185 | EUR 238.93 | Booked peak August; cancellation floor. | EUR 185 | EUR 205 | EUR 262.60 | Booked peak August; protect ADR if reopened. | EUR 205 | EUR 235 | EUR 298.10 | Booked peak August; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-08-13 | Thursday | 3 | EUR 195 | EUR 250.76 | Booked peak August; cancellation floor. | EUR 195 | EUR 215 | EUR 274.43 | Booked peak August; protect ADR if reopened. | EUR 215 | EUR 245 | EUR 309.93 | Booked peak August; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-08-14 | Friday | 3 | EUR 205 | EUR 262.60 | Booked peak August; cancellation floor. | EUR 205 | EUR 225 | EUR 286.26 | Booked peak August; protect ADR if reopened. | EUR 225 | EUR 255 | EUR 321.77 | Booked peak August; scarcity-led ceiling. | EUR 255 | Balanced if reopened |
| 2026-08-15 | Saturday | 3 | EUR 205 | EUR 262.60 | Booked peak August; cancellation floor. | EUR 205 | EUR 225 | EUR 286.26 | Booked peak August; protect ADR if reopened. | EUR 225 | EUR 255 | EUR 321.77 | Booked peak August; scarcity-led ceiling. | EUR 255 | Balanced if reopened |
| 2026-08-16 | Sunday | 3 | EUR 195 | EUR 250.76 | Booked peak August; cancellation floor. | EUR 195 | EUR 215 | EUR 274.43 | Booked peak August; protect ADR if reopened. | EUR 215 | EUR 245 | EUR 309.93 | Booked peak August; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-08-17 | Monday | 3 | EUR 185 | EUR 238.93 | Booked peak August; cancellation floor. | EUR 185 | EUR 205 | EUR 262.60 | Booked peak August; protect ADR if reopened. | EUR 205 | EUR 235 | EUR 298.10 | Booked peak August; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-08-18 | Tuesday | 3 | EUR 180 | EUR 233.01 | Booked peak August; cancellation floor. | EUR 180 | EUR 200 | EUR 256.68 | Booked peak August; protect ADR if reopened. | EUR 200 | EUR 230 | EUR 292.18 | Booked peak August; scarcity-led ceiling. | EUR 230 | Balanced if reopened |
| 2026-08-19 | Wednesday | 3 | EUR 190 | EUR 244.85 | Prime August 4-night gap, keep occupancy viable. | EUR 190 | EUR 210 | EUR 268.51 | Prime August scarcity, still below direct seafront gross. | EUR 210 | EUR 240 | EUR 304.02 | Prime August yield push for scarce gap. | EUR 240 | Aggressive |
| 2026-08-20 | Thursday | 3 | EUR 200 | EUR 256.68 | Prime August 4-night gap, keep occupancy viable. | EUR 200 | EUR 220 | EUR 280.35 | Prime August scarcity, still below direct seafront gross. | EUR 220 | EUR 250 | EUR 315.85 | Prime August yield push for scarce gap. | EUR 250 | Aggressive |
| 2026-08-21 | Friday | 3 | EUR 210 | EUR 268.51 | Prime August 4-night gap, keep occupancy viable. | EUR 210 | EUR 230 | EUR 292.18 | Prime August scarcity, still below direct seafront gross. | EUR 230 | EUR 260 | EUR 327.68 | Prime August yield push for scarce gap. | EUR 260 | Aggressive |
| 2026-08-22 | Saturday | 3 | EUR 210 | EUR 268.51 | Prime August 4-night gap, keep occupancy viable. | EUR 210 | EUR 230 | EUR 292.18 | Prime August scarcity, still below direct seafront gross. | EUR 230 | EUR 260 | EUR 327.68 | Prime August yield push for scarce gap. | EUR 260 | Aggressive |
| 2026-08-23 | Sunday | 3 | EUR 195 | EUR 250.76 | Late-August peak, protect remaining inventory. | EUR 195 | EUR 215 | EUR 274.43 | Late-August balanced yield with high internal occupancy. | EUR 215 | EUR 245 | EUR 309.93 | Late-August premium ceiling for scarce dates. | EUR 245 | Balanced if reopened |
| 2026-08-24 | Monday | 3 | EUR 185 | EUR 238.93 | Late-August peak, protect remaining inventory. | EUR 185 | EUR 205 | EUR 262.60 | Late-August balanced yield with high internal occupancy. | EUR 205 | EUR 235 | EUR 298.10 | Late-August premium ceiling for scarce dates. | EUR 235 | Balanced if reopened |
| 2026-08-25 | Tuesday | 3 | EUR 180 | EUR 233.01 | Late-August peak, protect remaining inventory. | EUR 180 | EUR 200 | EUR 256.68 | Late-August balanced yield with high internal occupancy. | EUR 200 | EUR 230 | EUR 292.18 | Late-August premium ceiling for scarce dates. | EUR 230 | Balanced if reopened |
| 2026-08-26 | Wednesday | 3 | EUR 185 | EUR 238.93 | Late-August peak, protect remaining inventory. | EUR 185 | EUR 205 | EUR 262.60 | Late-August balanced yield with high internal occupancy. | EUR 205 | EUR 235 | EUR 298.10 | Late-August premium ceiling for scarce dates. | EUR 235 | Balanced if reopened |
| 2026-08-27 | Thursday | 3 | EUR 195 | EUR 250.76 | Late-August peak, protect remaining inventory. | EUR 195 | EUR 215 | EUR 274.43 | Late-August balanced yield with high internal occupancy. | EUR 215 | EUR 245 | EUR 309.93 | Late-August premium ceiling for scarce dates. | EUR 245 | Balanced if reopened |
| 2026-08-28 | Friday | 3 | EUR 205 | EUR 262.60 | Late-August peak, protect remaining inventory. | EUR 205 | EUR 225 | EUR 286.26 | Late-August balanced yield with high internal occupancy. | EUR 225 | EUR 255 | EUR 321.77 | Late-August premium ceiling for scarce dates. | EUR 255 | Balanced if reopened |
| 2026-08-29 | Saturday | 3 | EUR 205 | EUR 262.60 | Late-August peak, protect remaining inventory. | EUR 205 | EUR 225 | EUR 286.26 | Late-August balanced yield with high internal occupancy. | EUR 225 | EUR 255 | EUR 321.77 | Late-August premium ceiling for scarce dates. | EUR 255 | Balanced if reopened |
| 2026-08-30 | Sunday | 3 | EUR 195 | EUR 250.76 | Late-August peak, protect remaining inventory. | EUR 195 | EUR 215 | EUR 274.43 | Late-August balanced yield with high internal occupancy. | EUR 215 | EUR 245 | EUR 309.93 | Late-August premium ceiling for scarce dates. | EUR 245 | Balanced if reopened |
| 2026-08-31 | Monday | 3 | EUR 185 | EUR 238.93 | Late-August peak, protect remaining inventory. | EUR 185 | EUR 205 | EUR 262.60 | Late-August balanced yield with high internal occupancy. | EUR 205 | EUR 235 | EUR 298.10 | Late-August premium ceiling for scarce dates. | EUR 235 | Aggressive |
| 2026-09-01 | Tuesday | 3 | EUR 180 | EUR 233.01 | Early-September gap, keep strong conversion. | EUR 180 | EUR 200 | EUR 256.68 | Early-September scarcity supports premium net. | EUR 200 | EUR 230 | EUR 292.18 | Early-September high-season ceiling. | EUR 230 | Aggressive |
| 2026-09-02 | Wednesday | 3 | EUR 185 | EUR 238.93 | Early-September gap, keep strong conversion. | EUR 185 | EUR 205 | EUR 262.60 | Early-September scarcity supports premium net. | EUR 205 | EUR 235 | EUR 298.10 | Early-September high-season ceiling. | EUR 235 | Aggressive |
| 2026-09-03 | Thursday | 3 | EUR 195 | EUR 250.76 | Early-September gap, keep strong conversion. | EUR 195 | EUR 215 | EUR 274.43 | Early-September scarcity supports premium net. | EUR 215 | EUR 245 | EUR 309.93 | Early-September high-season ceiling. | EUR 245 | Aggressive |
| 2026-09-04 | Friday | 3 | EUR 205 | EUR 262.60 | Early-September gap, keep strong conversion. | EUR 205 | EUR 225 | EUR 286.26 | Early-September scarcity supports premium net. | EUR 225 | EUR 255 | EUR 321.77 | Early-September high-season ceiling. | EUR 255 | Aggressive |
| 2026-09-05 | Saturday | 2 | EUR 195 | EUR 260.76 | Booked September; cancellation floor. | EUR 195 | EUR 215 | EUR 284.43 | Booked September; keep premium if reopened. | EUR 215 | EUR 245 | EUR 319.93 | Booked September; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-09-06 | Sunday | 2 | EUR 185 | EUR 248.93 | Booked September; cancellation floor. | EUR 185 | EUR 205 | EUR 272.60 | Booked September; keep premium if reopened. | EUR 205 | EUR 235 | EUR 308.10 | Booked September; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-09-07 | Monday | 2 | EUR 175 | EUR 237.09 | Booked September; cancellation floor. | EUR 175 | EUR 195 | EUR 260.76 | Booked September; keep premium if reopened. | EUR 195 | EUR 225 | EUR 296.26 | Booked September; scarcity-led ceiling. | EUR 225 | Balanced if reopened |
| 2026-09-08 | Tuesday | 2 | EUR 170 | EUR 231.18 | Booked September; cancellation floor. | EUR 170 | EUR 190 | EUR 254.85 | Booked September; keep premium if reopened. | EUR 190 | EUR 220 | EUR 290.35 | Booked September; scarcity-led ceiling. | EUR 220 | Balanced if reopened |
| 2026-09-09 | Wednesday | 2 | EUR 175 | EUR 237.09 | Booked September; cancellation floor. | EUR 175 | EUR 195 | EUR 260.76 | Booked September; keep premium if reopened. | EUR 195 | EUR 225 | EUR 296.26 | Booked September; scarcity-led ceiling. | EUR 225 | Balanced if reopened |
| 2026-09-10 | Thursday | 2 | EUR 185 | EUR 248.93 | Booked September; cancellation floor. | EUR 185 | EUR 205 | EUR 272.60 | Booked September; keep premium if reopened. | EUR 205 | EUR 235 | EUR 308.10 | Booked September; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-09-11 | Friday | 2 | EUR 195 | EUR 260.76 | Booked September; cancellation floor. | EUR 195 | EUR 215 | EUR 284.43 | Booked September; keep premium if reopened. | EUR 215 | EUR 245 | EUR 319.93 | Booked September; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-09-12 | Saturday | 2 | EUR 195 | EUR 260.76 | Booked September; cancellation floor. | EUR 195 | EUR 215 | EUR 284.43 | Booked September; keep premium if reopened. | EUR 215 | EUR 245 | EUR 319.93 | Booked September; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-09-13 | Sunday | 2 | EUR 185 | EUR 248.93 | Booked September; cancellation floor. | EUR 185 | EUR 205 | EUR 272.60 | Booked September; keep premium if reopened. | EUR 205 | EUR 235 | EUR 308.10 | Booked September; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-09-14 | Monday | 2 | EUR 175 | EUR 237.09 | Booked September; cancellation floor. | EUR 175 | EUR 195 | EUR 260.76 | Booked September; keep premium if reopened. | EUR 195 | EUR 225 | EUR 296.26 | Booked September; scarcity-led ceiling. | EUR 225 | Balanced if reopened |
| 2026-09-15 | Tuesday | 2 | EUR 170 | EUR 231.18 | Booked September; cancellation floor. | EUR 170 | EUR 190 | EUR 254.85 | Booked September; keep premium if reopened. | EUR 190 | EUR 220 | EUR 290.35 | Booked September; scarcity-led ceiling. | EUR 220 | Balanced if reopened |
| 2026-09-16 | Wednesday | 2 | EUR 175 | EUR 237.09 | Booked September; cancellation floor. | EUR 175 | EUR 195 | EUR 260.76 | Booked September; keep premium if reopened. | EUR 195 | EUR 225 | EUR 296.26 | Booked September; scarcity-led ceiling. | EUR 225 | Balanced if reopened |
| 2026-09-17 | Thursday | 2 | EUR 185 | EUR 248.93 | Booked September; cancellation floor. | EUR 185 | EUR 205 | EUR 272.60 | Booked September; keep premium if reopened. | EUR 205 | EUR 235 | EUR 308.10 | Booked September; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-09-18 | Friday | 2 | EUR 195 | EUR 260.76 | Booked September; cancellation floor. | EUR 195 | EUR 215 | EUR 284.43 | Booked September; keep premium if reopened. | EUR 215 | EUR 245 | EUR 319.93 | Booked September; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-09-19 | Saturday | 2 | EUR 195 | EUR 260.76 | Booked September; cancellation floor. | EUR 195 | EUR 215 | EUR 284.43 | Booked September; keep premium if reopened. | EUR 215 | EUR 245 | EUR 319.93 | Booked September; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-09-20 | Sunday | 2 | EUR 185 | EUR 248.93 | Booked September; cancellation floor. | EUR 185 | EUR 205 | EUR 272.60 | Booked September; keep premium if reopened. | EUR 205 | EUR 235 | EUR 308.10 | Booked September; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-09-21 | Monday | 2 | EUR 175 | EUR 237.09 | Booked September; cancellation floor. | EUR 175 | EUR 195 | EUR 260.76 | Booked September; keep premium if reopened. | EUR 195 | EUR 225 | EUR 296.26 | Booked September; scarcity-led ceiling. | EUR 225 | Balanced if reopened |
| 2026-09-22 | Tuesday | 2 | EUR 170 | EUR 231.18 | Booked September; cancellation floor. | EUR 170 | EUR 190 | EUR 254.85 | Booked September; keep premium if reopened. | EUR 190 | EUR 220 | EUR 290.35 | Booked September; scarcity-led ceiling. | EUR 220 | Balanced if reopened |
| 2026-09-23 | Wednesday | 2 | EUR 175 | EUR 237.09 | Booked September; cancellation floor. | EUR 175 | EUR 195 | EUR 260.76 | Booked September; keep premium if reopened. | EUR 195 | EUR 225 | EUR 296.26 | Booked September; scarcity-led ceiling. | EUR 225 | Balanced if reopened |
| 2026-09-24 | Thursday | 2 | EUR 185 | EUR 248.93 | Booked September; cancellation floor. | EUR 185 | EUR 205 | EUR 272.60 | Booked September; keep premium if reopened. | EUR 205 | EUR 235 | EUR 308.10 | Booked September; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-09-25 | Friday | 2 | EUR 195 | EUR 260.76 | Booked September; cancellation floor. | EUR 195 | EUR 215 | EUR 284.43 | Booked September; keep premium if reopened. | EUR 215 | EUR 245 | EUR 319.93 | Booked September; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-09-26 | Saturday | 2 | EUR 195 | EUR 260.76 | Booked September; cancellation floor. | EUR 195 | EUR 215 | EUR 284.43 | Booked September; keep premium if reopened. | EUR 215 | EUR 245 | EUR 319.93 | Booked September; scarcity-led ceiling. | EUR 245 | Balanced if reopened |
| 2026-09-27 | Sunday | 2 | EUR 185 | EUR 248.93 | Booked September; cancellation floor. | EUR 185 | EUR 205 | EUR 272.60 | Booked September; keep premium if reopened. | EUR 205 | EUR 235 | EUR 308.10 | Booked September; scarcity-led ceiling. | EUR 235 | Balanced if reopened |
| 2026-09-28 | Monday | 2 | EUR 175 | EUR 237.09 | Booked September; cancellation floor. | EUR 175 | EUR 195 | EUR 260.76 | Booked September; keep premium if reopened. | EUR 195 | EUR 225 | EUR 296.26 | Booked September; scarcity-led ceiling. | EUR 225 | Balanced if reopened |
| 2026-09-29 | Tuesday | 2 | EUR 170 | EUR 231.18 | Booked September; cancellation floor. | EUR 170 | EUR 190 | EUR 254.85 | Booked September; keep premium if reopened. | EUR 190 | EUR 220 | EUR 290.35 | Booked September; scarcity-led ceiling. | EUR 220 | Balanced if reopened |
| 2026-09-30 | Wednesday | 2 | EUR 175 | EUR 237.09 | Booked September; cancellation floor. | EUR 175 | EUR 195 | EUR 260.76 | Booked September; keep premium if reopened. | EUR 195 | EUR 225 | EUR 296.26 | Booked September; scarcity-led ceiling. | EUR 225 | Balanced if reopened |
| 2026-10-01 | Thursday | 2 | EUR 140 | EUR 195.68 | October shoulder, occupancy-first rate. | EUR 140 | EUR 160 | EUR 219.34 | October shoulder, premium but searchable. | EUR 160 | EUR 185 | EUR 248.93 | October shoulder yield push for weekends/lead time. | EUR 185 | Balanced |
| 2026-10-02 | Friday | 2 | EUR 150 | EUR 207.51 | October shoulder, occupancy-first rate. | EUR 150 | EUR 170 | EUR 231.18 | October shoulder, premium but searchable. | EUR 170 | EUR 195 | EUR 260.76 | October shoulder yield push for weekends/lead time. | EUR 195 | Balanced |
| 2026-10-03 | Saturday | 2 | EUR 150 | EUR 207.51 | October shoulder, occupancy-first rate. | EUR 150 | EUR 170 | EUR 231.18 | October shoulder, premium but searchable. | EUR 170 | EUR 195 | EUR 260.76 | October shoulder yield push for weekends/lead time. | EUR 195 | Balanced |
| 2026-10-04 | Sunday | 2 | EUR 140 | EUR 195.68 | October shoulder, occupancy-first rate. | EUR 140 | EUR 160 | EUR 219.34 | October shoulder, premium but searchable. | EUR 160 | EUR 185 | EUR 248.93 | October shoulder yield push for weekends/lead time. | EUR 185 | Balanced |
| 2026-10-05 | Monday | 2 | EUR 130 | EUR 183.84 | October shoulder, occupancy-first rate. | EUR 130 | EUR 150 | EUR 207.51 | October shoulder, premium but searchable. | EUR 150 | EUR 175 | EUR 237.09 | October shoulder yield push for weekends/lead time. | EUR 175 | Balanced |
| 2026-10-06 | Tuesday | 2 | EUR 125 | EUR 177.93 | October shoulder, occupancy-first rate. | EUR 125 | EUR 145 | EUR 201.59 | October shoulder, premium but searchable. | EUR 145 | EUR 170 | EUR 231.18 | October shoulder yield push for weekends/lead time. | EUR 170 | Balanced |
| 2026-10-07 | Wednesday | 2 | EUR 130 | EUR 183.84 | October shoulder, occupancy-first rate. | EUR 130 | EUR 150 | EUR 207.51 | October shoulder, premium but searchable. | EUR 150 | EUR 175 | EUR 237.09 | October shoulder yield push for weekends/lead time. | EUR 175 | Balanced |
| 2026-10-08 | Thursday | 2 | EUR 140 | EUR 195.68 | October shoulder, occupancy-first rate. | EUR 140 | EUR 160 | EUR 219.34 | October shoulder, premium but searchable. | EUR 160 | EUR 185 | EUR 248.93 | October shoulder yield push for weekends/lead time. | EUR 185 | Balanced |
| 2026-10-09 | Friday | 2 | EUR 150 | EUR 207.51 | October shoulder, occupancy-first rate. | EUR 150 | EUR 170 | EUR 231.18 | October shoulder, premium but searchable. | EUR 170 | EUR 195 | EUR 260.76 | October shoulder yield push for weekends/lead time. | EUR 195 | Balanced |
| 2026-10-10 | Saturday | 2 | EUR 150 | EUR 207.51 | October shoulder, occupancy-first rate. | EUR 150 | EUR 170 | EUR 231.18 | October shoulder, premium but searchable. | EUR 170 | EUR 195 | EUR 260.76 | October shoulder yield push for weekends/lead time. | EUR 195 | Balanced |
| 2026-10-11 | Sunday | 2 | EUR 140 | EUR 195.68 | October shoulder, occupancy-first rate. | EUR 140 | EUR 160 | EUR 219.34 | October shoulder, premium but searchable. | EUR 160 | EUR 185 | EUR 248.93 | October shoulder yield push for weekends/lead time. | EUR 185 | Balanced |
| 2026-10-12 | Monday | 2 | EUR 130 | EUR 183.84 | October shoulder, occupancy-first rate. | EUR 130 | EUR 150 | EUR 207.51 | October shoulder, premium but searchable. | EUR 150 | EUR 175 | EUR 237.09 | October shoulder yield push for weekends/lead time. | EUR 175 | Balanced |
| 2026-10-13 | Tuesday | 2 | EUR 125 | EUR 177.93 | October shoulder, occupancy-first rate. | EUR 125 | EUR 145 | EUR 201.59 | October shoulder, premium but searchable. | EUR 145 | EUR 170 | EUR 231.18 | October shoulder yield push for weekends/lead time. | EUR 170 | Balanced |
| 2026-10-14 | Wednesday | 2 | EUR 130 | EUR 183.84 | October shoulder, occupancy-first rate. | EUR 130 | EUR 150 | EUR 207.51 | October shoulder, premium but searchable. | EUR 150 | EUR 175 | EUR 237.09 | October shoulder yield push for weekends/lead time. | EUR 175 | Balanced |
| 2026-10-15 | Thursday | 2 | EUR 140 | EUR 195.68 | October shoulder, occupancy-first rate. | EUR 140 | EUR 160 | EUR 219.34 | October shoulder, premium but searchable. | EUR 160 | EUR 185 | EUR 248.93 | October shoulder yield push for weekends/lead time. | EUR 185 | Balanced |
| 2026-10-16 | Friday | 2 | EUR 140 | EUR 195.68 | Late-October softer demand, protect conversion. | EUR 140 | EUR 160 | EUR 219.34 | Late-October shoulder rate with quality signal. | EUR 160 | EUR 185 | EUR 248.93 | Late-October premium test on stronger nights. | EUR 185 | Balanced |
| 2026-10-17 | Saturday | 2 | EUR 140 | EUR 195.68 | Late-October softer demand, protect conversion. | EUR 140 | EUR 160 | EUR 219.34 | Late-October shoulder rate with quality signal. | EUR 160 | EUR 185 | EUR 248.93 | Late-October premium test on stronger nights. | EUR 185 | Balanced |
| 2026-10-18 | Sunday | 2 | EUR 130 | EUR 183.84 | Late-October softer demand, protect conversion. | EUR 130 | EUR 150 | EUR 207.51 | Late-October shoulder rate with quality signal. | EUR 150 | EUR 175 | EUR 237.09 | Late-October premium test on stronger nights. | EUR 175 | Balanced |
| 2026-10-19 | Monday | 2 | EUR 120 | EUR 172.01 | Late-October softer demand, protect conversion. | EUR 120 | EUR 140 | EUR 195.68 | Late-October shoulder rate with quality signal. | EUR 140 | EUR 165 | EUR 225.26 | Late-October premium test on stronger nights. | EUR 165 | Balanced |
| 2026-10-20 | Tuesday | 2 | EUR 115 | EUR 166.09 | Late-October softer demand, protect conversion. | EUR 115 | EUR 135 | EUR 189.76 | Late-October shoulder rate with quality signal. | EUR 135 | EUR 160 | EUR 219.34 | Late-October premium test on stronger nights. | EUR 160 | Balanced |
| 2026-10-21 | Wednesday | 2 | EUR 120 | EUR 172.01 | Late-October softer demand, protect conversion. | EUR 120 | EUR 140 | EUR 195.68 | Late-October shoulder rate with quality signal. | EUR 140 | EUR 165 | EUR 225.26 | Late-October premium test on stronger nights. | EUR 165 | Balanced |
| 2026-10-22 | Thursday | 2 | EUR 130 | EUR 183.84 | Late-October softer demand, protect conversion. | EUR 130 | EUR 150 | EUR 207.51 | Late-October shoulder rate with quality signal. | EUR 150 | EUR 175 | EUR 237.09 | Late-October premium test on stronger nights. | EUR 175 | Balanced |
| 2026-10-23 | Friday | 2 | EUR 140 | EUR 195.68 | Late-October softer demand, protect conversion. | EUR 140 | EUR 160 | EUR 219.34 | Late-October shoulder rate with quality signal. | EUR 160 | EUR 185 | EUR 248.93 | Late-October premium test on stronger nights. | EUR 185 | Balanced |
| 2026-10-24 | Saturday | 2 | EUR 140 | EUR 195.68 | Late-October softer demand, protect conversion. | EUR 140 | EUR 160 | EUR 219.34 | Late-October shoulder rate with quality signal. | EUR 160 | EUR 185 | EUR 248.93 | Late-October premium test on stronger nights. | EUR 185 | Balanced |
| 2026-10-25 | Sunday | 2 | EUR 130 | EUR 183.84 | Late-October softer demand, protect conversion. | EUR 130 | EUR 150 | EUR 207.51 | Late-October shoulder rate with quality signal. | EUR 150 | EUR 175 | EUR 237.09 | Late-October premium test on stronger nights. | EUR 175 | Balanced |
| 2026-10-26 | Monday | 2 | EUR 120 | EUR 172.01 | Late-October softer demand, protect conversion. | EUR 120 | EUR 140 | EUR 195.68 | Late-October shoulder rate with quality signal. | EUR 140 | EUR 165 | EUR 225.26 | Late-October premium test on stronger nights. | EUR 165 | Balanced |
| 2026-10-27 | Tuesday | 2 | EUR 115 | EUR 166.09 | Late-October softer demand, protect conversion. | EUR 115 | EUR 135 | EUR 189.76 | Late-October shoulder rate with quality signal. | EUR 135 | EUR 160 | EUR 219.34 | Late-October premium test on stronger nights. | EUR 160 | Balanced |
| 2026-10-28 | Wednesday | 2 | EUR 120 | EUR 172.01 | Late-October softer demand, protect conversion. | EUR 120 | EUR 140 | EUR 195.68 | Late-October shoulder rate with quality signal. | EUR 140 | EUR 165 | EUR 225.26 | Late-October premium test on stronger nights. | EUR 165 | Balanced |
| 2026-10-29 | Thursday | 2 | EUR 130 | EUR 183.84 | Late-October softer demand, protect conversion. | EUR 130 | EUR 150 | EUR 207.51 | Late-October shoulder rate with quality signal. | EUR 150 | EUR 175 | EUR 237.09 | Late-October premium test on stronger nights. | EUR 175 | Balanced |
| 2026-10-30 | Friday | 2 | EUR 140 | EUR 195.68 | Late-October softer demand, protect conversion. | EUR 140 | EUR 160 | EUR 219.34 | Late-October shoulder rate with quality signal. | EUR 160 | EUR 185 | EUR 248.93 | Late-October premium test on stronger nights. | EUR 185 | Balanced |
| 2026-10-31 | Saturday | 2 | EUR 140 | EUR 195.68 | Late-October softer demand, protect conversion. | EUR 140 | EUR 160 | EUR 219.34 | Late-October shoulder rate with quality signal. | EUR 160 | EUR 185 | EUR 248.93 | Late-October premium test on stronger nights. | EUR 185 | Balanced |
| 2026-11-01 | Sunday | 2 | EUR 115 | EUR 166.09 | Early-November conversion-led floor. | EUR 115 | EUR 130 | EUR 183.84 | Early-November balanced low-season entry. | EUR 130 | EUR 155 | EUR 213.43 | Early-November premium test for quality signal. | EUR 155 | Conservative |
| 2026-11-02 | Monday | 2 | EUR 105 | EUR 154.26 | Early-November conversion-led floor. | EUR 105 | EUR 120 | EUR 172.01 | Early-November balanced low-season entry. | EUR 120 | EUR 145 | EUR 201.59 | Early-November premium test for quality signal. | EUR 145 | Conservative |
| 2026-11-03 | Tuesday | 2 | EUR 100 | EUR 148.34 | Early-November conversion-led floor. | EUR 100 | EUR 115 | EUR 166.09 | Early-November balanced low-season entry. | EUR 115 | EUR 140 | EUR 195.68 | Early-November premium test for quality signal. | EUR 140 | Conservative |
| 2026-11-04 | Wednesday | 2 | EUR 105 | EUR 154.26 | Early-November conversion-led floor. | EUR 105 | EUR 120 | EUR 172.01 | Early-November balanced low-season entry. | EUR 120 | EUR 145 | EUR 201.59 | Early-November premium test for quality signal. | EUR 145 | Conservative |
| 2026-11-05 | Thursday | 2 | EUR 115 | EUR 166.09 | Early-November conversion-led floor. | EUR 115 | EUR 130 | EUR 183.84 | Early-November balanced low-season entry. | EUR 130 | EUR 155 | EUR 213.43 | Early-November premium test for quality signal. | EUR 155 | Conservative |

## Bottom Summary

The proposed strategy now separates stale July inventory from genuinely scarce peak inventory. For remaining July open gaps, use the conversion-rescue bands: EUR 115-EUR 145 net on weak weekdays, EUR 140-EUR 160 net on close weekends, and EUR 170-EUR 180 net only for stronger late-July Fridays/Saturdays. For the scarce August and early September gaps, keep Aggressive pricing unless a cancellation creates a very short lead-time problem. For October, taper into Balanced shoulder-season rates, and for early November shift toward Conservative/Balanced conversion support.

Confidence level: Medium. Confidence is strong on internal constraints, internal occupancy, and the closest direct Gzira comparison. It is weaker on broad market ceilings because several external competitor groups include scraping artifacts and some comparable sets have only one or two usable properties.

## assistantContext

### Checklist

- (Done) Internal group ID confirmed: `1bedroom-very-high-finish-very-high-amenities-side-sea-view-gzira`.
- (Done) AirGPT conversation ID created before report calls.
- (Done) Current UTC timestamp retrieved from AirGPT current-time tool.
- (Done) Competitor metadata retrieved.
- (Done) Internal constraints retrieved: EUR 90 minimum net rate, EUR 60 cleaning fee, open for bookings, launched 2025-10-27.
- (Done) Available report dates retrieved for selected internal and competitor groups.
- (Done) Multiple report snapshots reviewed for internal group and closest competitors.
- (Done) Competitor outliers and unreliable groups normalized or flagged.
- (Done) Immediate, short-term, medium-term, discount, and four-month calendar recommendations produced.
- (Done) July stale-inventory revision added after host reported exposed July dates were not selling.

### Data Sources Consulted

- `generateConversationId`: `019f32fe-2bed-74be-89f7-1507124bded5`.
- `getCurrentDateTime`: `2026-07-05T15:58:10.602860400` UTC.
- `getCompetitorMetadata`: internal group and all available competitor group metadata.
- `getConsumerGroupAvailableReportDays`: internal group, direct Gzira 1BR seafront, lower Gzira 1BR close-seafront, medium Gzira terrace, Sliema 1BR, Sliema 2BR seafront, premium Sliema 2BR seafront.
- `getCompetitorGroupReport`: internal snapshots on 2026-06-15, 2026-06-25, 2026-07-04; direct Gzira seafront snapshots on 2026-06-29 and 2026-07-05; lower Gzira close-seafront snapshots on 2026-06-25 and 2026-07-04; boundary snapshots for medium Gzira, Sliema 1BR, and Sliema 2BR groups on 2026-07-05.

### Information Accrued

- Internal listing title: Designer Penthouse by the Promenade | BBQ Terrace.
- Internal attributes: 1 bedroom, Gzira, very close to seafront, very high finish, very high amenities, side sea view, terrace, barbecue, no pool, max occupancy 4.
- Internal latest occupancy: July 43%, August 84%, September 87%.
- Internal latest gross averages: short-term EUR 178.20, July EUR 200.10, August EUR 192.83, September EUR 231.00.
- Direct Gzira 1BR seafront latest gross averages: immediate EUR 223.38, short-term EUR 233.50, July EUR 241.23, August EUR 290.60, September EUR 265.63.
- Lower-quality close Gzira 1BR groups show strong July occupancy but much lower August/September price bands.
- Competitor momentum for the same July span is mixed: cheaper/lower-quality stock moved, but premium and mid-premium gaps around July 13-18 remained open or were cut materially.
- Direct seafront 1BR stayed open for July 14-18 around EUR 233.50 gross, while Nevermind dropped comparable spans from about EUR 204.50 to EUR 153.50 gross and Juliet dropped near-term spans from about EUR 179-EUR 193 to EUR 147-EUR 172 gross.
- Several competitor reports contain impossible sub-EUR 1 values and were excluded from pricing calculations.

### Competitor Weights

| Group | Weight | Reason |
|---|---:|---|
| Internal group: very high finish side sea-view Gzira | 35% | Best evidence for constraints, demand, conversion, and calendar scarcity. |
| 1BR high finish/high amenities seafront-view Gzira | 25% | Closest premium local price ceiling, though prime sea view makes it slightly superior on view. |
| 1BR medium finish/medium amenities close-seafront Gzira | 12% | Useful lower local floor and availability signal. |
| Gzira medium terrace group | 12% | Terrace and local proximity are useful, but quality and data spread are mixed. |
| 1BR high finish no-view Sliema | 8% | Premium-area 1BR boundary, less direct due no view and possible metadata location inconsistency. |
| 2BR medium seafront Sliema | 5% | Upper market boundary only; different bedroom count and artifact risk. |
| 2BR very-high seafront Sliema | 3% | Very low weight due bedroom mismatch and repeated price artifacts. |

### Pricing Constraints Applied

- Minimum net rate floor: EUR 90.
- Cleaning fee: EUR 60, amortized by suggested minimum stay.
- Airbnb service fee gross-up: 18.34%.
- Base rates rounded to nearest EUR 5.
- Discounts stacked sequentially and rounded to the nearest EUR 5 after the floor is applied.

### Calculation Summary

- Proposed gross prices use `net * 1.1834 + 60 / minimumStay`.
- Revised July rescue examples for 2-night stays: EUR 125 net equals EUR 177.93 gross; EUR 145 net equals EUR 201.59 gross; EUR 160 net equals EUR 219.34 gross.
- For 2-night stays, EUR 165 net equals EUR 225.26 gross; EUR 175 net equals EUR 237.10 gross.
- For 3-night stays, EUR 215 net equals EUR 274.43 gross; EUR 245 net equals EUR 309.93 gross.
- The revised July figures intentionally sit below stale mid-premium competitors for weak weekdays, while August Aggressive pricing remains near the valid premium-market ceiling without copying unreliable Sliema artifact data.

### Assumptions

- Dates shown as booked in the internal report should not be actively repriced unless cancellation inventory reopens.
- The calendar dates are treated as check-in or stay-date pricing guidance; minimum stay recommendations protect open gaps while preserving visibility.
- User-provided stale-exposure information is treated as a meaningful conversion signal and overrides the earlier default Balanced July posture.
- No external event website was used because competitor and internal data were sufficient and no clean event-specific anomaly required validation.
- October and early November rely more on Malta seasonality and pricing framework because competitor reports supplied stronger coverage through September than through the full four-month horizon.

### Confidence

Medium. The recommendation is well supported for July through early September by internal and close competitor data. Confidence is lower for October and early November because the available competitor snapshots are less directly informative beyond September and because multiple boundary reports contain unreliable price artifacts.
