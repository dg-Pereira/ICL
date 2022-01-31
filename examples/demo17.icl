def comp = (fun f:Fun, g:Fun -> (fun x:int -> f(g(x)))) in
	def inc = (fun x:Int -> x+1) in
		def dup = comp(inc, inc) in
			dup(2)
		end
	end
end ;;