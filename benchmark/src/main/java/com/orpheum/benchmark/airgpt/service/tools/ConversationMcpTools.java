package com.orpheum.benchmark.airgpt.service.tools;

import com.orpheum.benchmark.competitor.support.UUIDs;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

@Component
public class ConversationMcpTools {

    @McpTool(
            name = "generateConversationId",
            description = """
            Generates a new conversation ID for a user session.

            Invoke this tool exactly once when starting a new conversation.
            The returned conversationId must be supplied to all subsequent
            MCP tool calls that belong to the same conversation.
            """
    )
    public ConversationIdResponse generateConversationId() {
        return new ConversationIdResponse(UUIDs.create().toString());
    }

    public record ConversationIdResponse(String conversationId) {
    }

}
