import React from "react";

/**
 * Props:
 *  - msg: { sender, content, timestamp }
 */
export default function Message({ msg }) {
  const isUser = msg.sender === "User" || msg.sender === "You";
  const time = msg.timestamp
    ? new Date(msg.timestamp).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })
    : "";

  return (
    <div className={`msg-row ${isUser ? "msg-row--user" : "msg-row--bot"}`}>
      <div className={`bubble ${isUser ? "bubble--user" : "bubble--bot"}`}>
        <div className="bubble-text">{msg.content}</div>
        <div className="bubble-time">{time}</div>
      </div>
    </div>
  );
}
