import React, { useState } from "react";

export default function InputBar({ onSend }) {
  const [text, setText] = useState("");

  const submit = (e) => {
    e?.preventDefault();
    const t = text.trim();
    if (!t) return;
    onSend(t);
    setText("");
  };

  return (
    <form className="inputbar" onSubmit={submit}>
      <input
        aria-label="Type a message"
        className="inputbar__input"
        value={text}
        onChange={(e) => setText(e.target.value)}
        placeholder="Type a message..."
      />
      <button type="submit" className="inputbar__send" aria-label="Send message">
        Send
      </button>
    </form>
  );
}
