import React from "react";
import NavBar from "./NavBar";
import { Flex } from "@chakra-ui/react";

function Layout({ children }: { children: React.ReactNode }) {
  return (
    <>
      <NavBar />
      <Flex bg="#383c4a" width={"full"} wrap="nowrap">
        {children}
      </Flex>
    </>
  );
}

export default Layout;
