export type UIBlock = {
  type: string;
  props?: Record<string, any>;
  children?: UIBlock[];
};

export type UISchemaResponse = { 
  blocks: UIBlock[]; 
  version?:string; 
};

