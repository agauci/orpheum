CREATE TABLE public.airgpt_conversation (
    id uuid PRIMARY KEY,
    title text NOT NULL,
    timestamp_created timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE public.airgpt_conversation_prompt (
    id uuid PRIMARY KEY,
    conversation_id uuid NOT NULL,
    "content" text NOT NULL,
    "text" text NOT NULL,
    structured_output json NULL,
    "type" varchar(10) NOT NULL,
    assistant_mode varchar(50) NULL,
    assistant_context TEXT,
    prompt_duration int4 NULL,
    gpt_model varchar(50) NULL,
    "timestamp" timestamp NOT NULL,
    CONSTRAINT airgpt_conversation_prompt_type_check CHECK (((type)::text = ANY ((ARRAY['USER'::character varying, 'ASSISTANT'::character varying, 'SYSTEM'::character varying, 'TOOL'::character varying])::text[])))
);
CREATE INDEX idx_airgpt_conversation_prompt_by_conversation_id_timestamp ON public.airgpt_conversation_prompt USING btree (conversation_id, "timestamp");

CREATE TABLE competitor_group_report (
    id BIGSERIAL PRIMARY KEY,
    timestamp_generated TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    group_id varchar(200) NOT NULL,
    group_title varchar(200) NOT NULL,
    group_report TEXT NOT NULL
);
CREATE INDEX idx_competitor_group_report_by_group_id ON competitor_group_report USING btree (group_id);

CREATE TABLE competitor_report (
    id BIGSERIAL PRIMARY KEY,
    competitor_group_report_id BIGINT,
    timestamp_generated TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    competitor_id varchar(200) NOT NULL,
    competitor_title varchar(200) NOT NULL,
    competitor_report TEXT NOT NULL
);
CREATE INDEX idx_competitor_report_by_competitor_id ON competitor_report USING btree (competitor_id);