import React, { useState, useEffect, useRef } from "react";
import Message from "./Message";
import InputBar from "./InputBar";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

export default function ChatWindow() {
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [stompClient, setStompClient] = useState(null);
  const chatEndRef = useRef(null);

  // Auto-scroll to bottom when messages update
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // Connect to WebSocket server
  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws-chat");
    const client = Stomp.over(socket);

    client.connect({}, () => {
      console.log("Connected to WebSocket");
      setStompClient(client);

      // Subscribe to topic for incoming bot responses
      client.subscribe("/topic/messages", (msg) => {
        const message = JSON.parse(msg.body);
    
        setMessages((prev) => [
          ...prev,
          { id: Date.now(), text: message.content, sender: "Bot", timestamp: new Date().toISOString() },
        ]);
        setLoading(false);
      });
    });

    return () => {
      if (client) client.disconnect();
    };
  }, []);

  // Send message to backend
  const handleSend = (text) => {
    if (!text.trim()) return;
    const userMessage = { sender: "User", content: text };

    // Show message in UI immediately
    setMessages((prev) => [
      ...prev,
      { id: Date.now(), text, sender: "You", timestamp: new Date().toISOString() },
    ]);

    setLoading(true);

    if (stompClient && stompClient.connected) {
      stompClient.send("/app/chat", {}, JSON.stringify(userMessage));
    } else {
      console.error("WebSocket is not connected.");
      setLoading(false);
    }
  };

  return (
    <div className="w-full max-w-lg h-[80vh] flex flex-col bg-white rounded-2xl shadow-lg">
      <div className="flex items-center justify-center bg-blue-600 text-white py-3 rounded-t-2xl text-lg font-bold">
        AI Chatbot 🤖
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-2">
        {messages.length === 0 && !loading && (
          <p className="text-center text-gray-500">Start the conversation...</p>
        )}

        {messages.map((msg) => (
          <Message key={msg.id} msg={msg} />
        ))}

        {loading && (
          <div className="flex justify-start">
            <div className="px-4 py-2 bg-gray-300 text-black rounded-2xl animate-pulse">
              Typing...
            </div>
          </div>
        )}
        <div ref={chatEndRef} />
      </div>

      <InputBar onSend={handleSend} />
    </div>
  );
}
