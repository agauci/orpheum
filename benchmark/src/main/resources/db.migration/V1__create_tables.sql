CREATE TABLE public.airgpt_conversation (
    id uuid NOT NULL,
    user_id varchar(255) NOT NULL,
    timestamp_created timestamptz NOT NULL DEFAULT now(),
    timezone varchar(10) NULL,
    locale varchar(10) NULL,
    tags _text NULL,
    CONSTRAINT airgpt_conversation_pkey PRIMARY KEY (id)
);
CREATE INDEX idx_airgpt_conversation_by_user_id_timestamp_created ON public.airgpt_conversation USING btree (user_id, timestamp_created);

CREATE TABLE public.airgpt_conversation_prompt (
    id uuid NOT NULL,
    conversation_id uuid NOT NULL,
    "content" text NOT NULL,
    "text" text NOT NULL,
    structured_output json NULL,
    "type" varchar(10) NOT NULL,
    assistant_mode varchar(50) NULL,
    prompt_duration int4 NULL,
    gpt_model varchar(50) NULL,
    "timestamp" timestamp NOT NULL,
    user_id varchar(255) NULL,
    CONSTRAINT airgpt_conversation_prompt_pkey PRIMARY KEY (id),
    CONSTRAINT airgpt_conversation_prompt_type_check CHECK (((type)::text = ANY ((ARRAY['USER'::character varying, 'ASSISTANT'::character varying, 'SYSTEM'::character varying, 'TOOL'::character varying])::text[])))
);
CREATE INDEX idx_airgpt_conversation_prompt_by_conversation_id_timestamp ON public.airgpt_conversation_prompt USING btree (conversation_id, "timestamp");
CREATE INDEX idx_airgpt_conversation_prompt_by_user_id_timestamp ON public.airgpt_conversation_prompt USING btree (user_id, "timestamp");

CREATE TABLE property_group_report (
    id BIGSERIAL PRIMARY KEY,
    timestamp_generated TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    group_id varchar(50) NOT NULL,
    group_title varchar(100) NOT NULL,
    group_report TEXT NOT NULL
);
CREATE INDEX idx_property_group_report_by_group_id ON property_group_report USING btree (group_id);

CREATE TABLE competitor_report (
    id BIGSERIAL PRIMARY KEY,
    property_group_report_id BIGINT,
    timestamp_generated TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    competitor_id varchar(50) NOT NULL,
    competitor_title varchar(100) NOT NULL,
    competitor_report TEXT NOT NULL
);
CREATE INDEX idx_competitor_report_by_competitor_id ON competitor_report USING btree (competitor_id);