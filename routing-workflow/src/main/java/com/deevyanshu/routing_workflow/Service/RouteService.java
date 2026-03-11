package com.deevyanshu.routing_workflow.Service;

import com.deevyanshu.routing_workflow.Dto.Response;
import com.deevyanshu.routing_workflow.Dto.Routes;
import com.deevyanshu.routing_workflow.Dto.Routing_class;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RouteService {


    private ChatClient chatClient;

    public RouteService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Response route(String query)
    {
        Routing_class routingClass= this.chatClient.prompt().user(u->u.text("Classify this request:{query}").param("query",query))
                .call().entity(Routing_class.class);
        System.out.println("routing to: "+routingClass.routes());

        switch (routingClass.routes())
        {
            case ACCOUNT:
                return this.chatClient.prompt().system("You are an account security specialist. Follow these guidelines:\n" +
                        "\t\t\t\t\t\t\t1. Always start with \"Account Support Response:\"\n" +
                        "\t\t\t\t\t\t\t2. Prioritize account security and verification\n" +
                        "\t\t\t\t\t\t\t3. Provide clear steps for account recovery/changes\n" +
                        "\t\t\t\t\t\t\t4. Include security tips and warnings\n" +
                        "\t\t\t\t\t\t\t5. Set clear expectations for resolution time\n" +
                        "\n" +
                        "\t\t\t\t\t\t\tMaintain a serious, security-focused tone. ").user(query).call().entity(Response.class);

            case BILLING:
                return this.chatClient.prompt().system("You are a billing support specialist. Follow these guidelines:\n" +
                        "\t\t\t\t\t\t\t1. Always start with \"Billing Support Response:\"\n" +
                        "\t\t\t\t\t\t\t2. First acknowledge the specific billing issue\n" +
                        "\t\t\t\t\t\t\t3. Explain any charges or discrepancies clearly\n" +
                        "\t\t\t\t\t\t\t4. List concrete next steps with timeline\n" +
                        "\t\t\t\t\t\t\t5. End with payment options if relevant\n" +
                        "\n" +
                        "\t\t\t\t\t\t\tKeep responses professional but friendly.").user(query).call()
                        .entity(Response.class);

            case TECH_SUPPORT:
                return this.chatClient.prompt().system("You are a technical support engineer. Follow these guidelines:\n" +
                        "\t\t\t\t\t\t\t1. Always start with \"Technical Support Response:\"\n" +
                        "\t\t\t\t\t\t\t2. List exact steps to resolve the issue\n" +
                        "\t\t\t\t\t\t\t3. Include system requirements if relevant\n" +
                        "\t\t\t\t\t\t\t4. Provide workarounds for common problems\n" +
                        "\t\t\t\t\t\t\t5. End with escalation path if needed\n" +
                        "\n" +
                        "\t\t\t\t\t\t\tUse clear, numbered steps and technical details.").user(query).call()
                        .entity(Response.class);

            default:
                return null;
        }
    }


}
