import { MenuItem } from "@chakra-ui/react";

export function CustomMenuItem({
  text,
  ...props
}: {
  text: string;
  [props: string]: any;
}) {
  return (
    <MenuItem _hover={{ bg: "gray.800" }} bg="#212226" {...props}>
      {text}
    </MenuItem>
  );
}
