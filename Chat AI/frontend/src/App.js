import React, { useEffect, useRef, useState } from "react";
import Message from "./components/Message";
import InputBar from "./components/InputBar";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

export default function App() {
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);
  const clientRef = useRef(null);
  const endRef = useRef(null);

  useEffect(() => {
    // Example: connect to your backend WebSocket
    const socketUrl = "http://localhost:8080/ws-chat";
    const client = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe("/topic/public", (frame) => {
          try {
            const payload = JSON.parse(frame.body);
            setMessages((prev) => [
              ...prev,
              {
                sender: payload.sender ?? "ChatBot",
                content: payload.content ?? payload.text ?? "",
                timestamp: payload.timestamp ?? new Date().toISOString(),
              },
            ]);
          } catch (err) {
            console.error("Invalid message", err);
          } finally {
            setLoading(false);
          }
        });
      },
      onStompError: (frame) => {
        console.error("STOMP error", frame);
        setLoading(false);
      },
    });

    client.activate();
    clientRef.current = client;
    return () => client.deactivate();
  }, []);

  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, loading]);

  const sendMessage = (text) => {
    if (!text) return;
    const msg = { sender: "User", content: text, timestamp: new Date().toISOString() };

    // Add locally
    setMessages((prev) => [...prev, msg]);
    setLoading(true);

    // Publish to backend
    const client = clientRef.current;
    if (client && client.connected) {
      client.publish({ destination: "/app/sendMessage", body: JSON.stringify(msg) });
    } else {
      console.error("WebSocket not connected - falling back");
      setLoading(false);
    }
  };

  return (
    <div className="chat-root">
      <div className="chat-header">AI Chatbot</div>

      <div className="chat-window" role="log" aria-live="polite">
        {messages.length === 0 && !loading && (
          <div className="chat-empty">Say hi — your conversation will appear here</div>
        )}

        {messages.map((m, i) => (
          <Message key={i} msg={m} />
        ))}

        {loading && (
          <div className="typing-row">
            <div className="bubble bubble--bot typing">Typing…</div>
          </div>
        )}

        <div ref={endRef} />
      </div>

      <InputBar onSend={sendMessage} />
    </div>
  );
}
