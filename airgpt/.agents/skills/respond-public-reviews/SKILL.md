---
name: respond-public-reviews
description: "Draft, revise, and finalize public host responses to guest reviews using the language, warmth, and structure in ./Public Review Responses/responses.md. Use when the user asks to reply to an Airbnb/public guest review, create a host response, handle review feedback, or save an accepted response to the top of the review responses file."
---

# Respond Public Reviews

## Purpose

Create polished public review responses for Emily and Andrew in the same English voice used in `./Public Review Responses/responses.md`: warm, personal, grateful, sophisticated, specific to the guest's words, and never formulaic.

## Required Inputs

Require all of these before drafting:

- Guest name.
- Full guest review text.
- Star rating out of 5.

If any required input is missing, ask only for the missing item(s). Do not draft from placeholders such as "Guest" or "Name unknown".

## Workflow

1. Read `./Public Review Responses/responses.md` before drafting or saving so the latest accepted examples guide the style.
2. Draft one response unless the user explicitly asks for variants.
3. Mention the guest's name naturally in the response.
4. Acknowledge concrete context selectively instead of mirroring every point. Identify the emotional through-line of the review, such as comfort, care, ease, peacefulness, family convenience, recommendations, or issue resolution, then weave in one or two natural references. Avoid checklist-style replies that restate each compliment back to the guest.
5. Keep the tone graceful, personal, sophisticated, and sincere. Favor natural warmth such as "thank you", "we're so glad", "we're really happy", "it was a pleasure hosting you", "we would be delighted/love to welcome you back", and similar language from the corpus, but do not copy an existing response structure verbatim or lean on one gratitude phrase as default connective tissue.
6. Keep each answer fresh enough to feel personal, but do not chase novelty for its own sake. Before presenting the draft, compare it mentally against existing responses and any drafts in the current conversation. Adjust the opening, sentence rhythm, details, or closing only where repetition would make the response feel stale or formulaic.
7. Present the draft and ask whether the latest version is accepted/final or should be revised.
8. The task is not complete until the user confirms the latest version is accepted/final.
9. Only after explicit acceptance/final confirmation, insert the accepted entry at the top of `./Public Review Responses/responses.md` in the existing format so the newest accepted response appears first.

## Response Guidelines

Use the examples file as the source of truth, but follow these house rules:

- Use English unless the user explicitly asks for another language.
- Be generous and thankful when the review is positive.
- When a review goes above and beyond in generosity, specificity, or personal feeling, let the response rise to meet it. Use a more heartfelt register when earned, such as "we're truly grateful" or "we are thankful for every guest who chooses to stay with us", while keeping the tone grounded, elegant, and sincere rather than dramatic or performative.
- Do not default to the opener "Thank you so much for your lovely/kind review, [Name]." Use it when it is clearly the best fit, but prefer simple, direct openings over self-conscious ones. "Thank you for your kind words, [Name]" can be stronger than a clever opener. Avoid stiff or performative canned starts such as "We are so glad to read this", "It is lovely to hear this", or "This is such a lovely compliment".
- For positive specifics, synthesize rather than enumerate. If a review lists many strengths, respond to the overall feeling of the stay and mention only the most meaningful one or two details.
- When the review gives enough texture, make the response feel personally observed rather than merely polite: connect the guest's experience to the care put into the space, communication, and small touches.
- If the guest gives a memorable phrase, it can carry the emotional weight of the response. A simple opener followed by a quoted phrase and a warm reaction such as "made our day" often feels more natural than leading with a dramatic opener.
- If the guest offers an especially vivid or generous line, such as regretting not staying longer or comparing the stay favourably to a hotel, acknowledge it warmly and allow it to shape the response's feeling.
- Vary gratitude and transition phrasing across consecutive drafts. Avoid overusing "it means a lot to know" or "it means the world" when a simpler sentence would flow better.
- Prefer light, natural modifiers when they fit the voice; a simple "really" often works better than weightier words such as "especially" or "particularly" unless those words add useful precision.
- Do not over-polish away genuine warmth. A repeated simple word like "happy" or a tasteful exclamation mark can be right when the review is enthusiastic and the sentence sounds like Emily and Andrew speaking naturally.
- Vary closing invitations while keeping the same welcoming sentiment. Avoid defaulting to "we would be delighted to welcome you back anytime" in every response; use fresh, natural closings such as "we hope your travels bring you back this way", "we'd love to host you again on a future Malta trip", or "we'll be so pleased if you choose to return".
- Use tried-and-tested phrasing when it genuinely fits. A familiar line is not a problem if it sounds warm, elegant, and true to Emily and Andrew's voice; forced variation is worse than a graceful, reliable sentence.
- For constructive or mixed reviews, especially 4-star reviews, center gratitude, composure, and responsiveness. Keep the positives present, thank the guest for practical feedback, and show that feedback is helping improve the stay; avoid leading with apologies or amplifying negative points such as value concerns.
- When a real improvement has already been made, mention one concise, guest-facing action. Phrase changes in terms of what future guests will experience, such as updated guest information or an added welcome item. Avoid internal/process language such as "clearer", "setup messaging", or generic statements like "we updated our process" unless the wording feels natural to a guest.
- Avoid stiff or transactional sentences such as "thank you as well", "your feedback is appreciated", "we appreciate your business", or standalone acknowledgements of a recommendation. The response should sound like attentive hosts writing personally, not a customer-service script.
- Avoid over-apologizing for minor issues; acknowledge useful feedback with care and, where possible, show action.
- Do not invent facts, private interactions, or remedial actions that are not supported by the review or user-provided context.
- If the user confirms a real personal interaction, such as meeting the guest or their family, include it naturally near the closing when it adds warmth. Do not add in-person details unless they are explicitly provided.
- Match length to the review. Short enthusiastic reviews can receive one concise paragraph; detailed reviews usually deserve two or three short paragraphs.
- Do not use a rigid template. Avoid repeating the same opener, closer, or "thank you for being..." phrasing across consecutive responses.
- Mention the guest by name even when the response is short.

## Save Format

Insert exactly this shape at the top of `./Public Review Responses/responses.md`, preserving the existing markdown style:

```markdown
# Guest Name

Guest review:
Full review text

Stars (Out of 5):
5

Host response:
Accepted host response
```

Place exactly one `---` separator, surrounded by blank lines, between the new top entry and the previous first entry. Do not add a separator before the new top entry, and do not append accepted entries to the bottom. Preserve the guest review text exactly as provided unless the user asks to correct typos.
