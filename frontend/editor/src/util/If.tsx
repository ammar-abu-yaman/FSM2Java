import React from "react";

export function If({
  condition,
  children,
}: {
  condition: () => boolean;
  children: React.ReactNode;
}) {
  return condition() ? <>{children}</> : <></>;
}
