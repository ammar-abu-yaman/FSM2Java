import { RootType } from "../store";

export interface FsmJson {
  options: { name: string; value: string[] }[];
  states: {
    name: string;
    enterCode: string;
    exitCode: string;
    transitions: {
      name: string;
      params: { name: string; type: string }[];
      guard?: string;
      nextState?: string;
      code: string;
    }[];
  }[];
}

export function aggregateState(data: RootType): FsmJson {
  const { metaData } = data;
  const options = [
    { name: "class", value: [metaData.className] },
    { name: "package", value: [metaData.package] },
    { name: "initial-state", value: [metaData.initialState ?? ""] },
    { name: "actions", value: metaData.actions },
  ];

  const states = data.states.map((state) => ({
    name: state.name,
    enterCode: state.entryCode,
    exitCode: state.exitCode,
    transitions: data.transitions
      .filter((transition) => transition.sourceId.stateId === state.id)
      .map((transition) => ({
        name: transition.name,
        params: transition.parameters,
        guard: transition.condition,
        nextState: data.states.find(
          (state) => state.id === transition.destId.stateId
        )?.name,
        code: transition.code,
      })),
  }));

  return { options, states };
}
